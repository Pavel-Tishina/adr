package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.adapter.DuplicateFileAdapter
import com.paveltsikota.webcore.db.adapter.DuplicateHashAdapter
import com.paveltsikota.webcore.db.dto.DuplicateDto
import com.paveltsikota.webcore.db.dto.DuplicateFileDto
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.service.DuplicateService
import com.paveltsikota.webcore.db.service.FilesService
import com.paveltsikota.webcore.db.service.HashesService
import com.paveltsikota.webcore.db.service.result.DuplicateServiceOperationResult
import com.paveltsikota.webcore.db.service.result.enums.DuplicatesOperationResultType
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import org.springframework.stereotype.Service
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.collections.mutableSetOf

@Service
class DuplicateServiceImpl(
    private val filesService: FilesService,
    private val hashesService: HashesService,
    private val dupFilesAdapter: DuplicateFileAdapter,
    private val dupHashesAdapter: DuplicateHashAdapter
): DuplicateService {

    private val LOG = KotlinLogging.logger {}

    override fun getDuplicates(count: Int, page: Int, profileId: Long, dupN: Int): DuplicateServiceOperationResult {
        val hashesResult = hashesService.getAllByN(count, page , profileId, dupN)

        if (!hashesResult.success) {
            return DuplicateServiceOperationResult(success = false, error = hashesResult.error,
                result = DuplicatesOperationResultType.OBJECTS_NOT_FOUNDED)
        }

        val hashes = hashesResult.obj as List<HashesEntity>
        val duplicatesList = mutableListOf<DuplicateDto>()

        hashes.forEach { h ->
            val filesResult = filesService.getFiles(h.duplicates)
            if (filesResult.success) {
                val files = filesResult.obj as List<FilesEntity>

                val duplicateFilesSet = mutableSetOf<DuplicateFileDto>()
                files.forEach { duplicateFilesSet.add(dupFilesAdapter.entityToDto(it)) }

                duplicatesList.add(
                    DuplicateDto(
                        id = h.id,
                        size = h.size,
                        profile = h.profile,
                        hash = h.hash,
                        hashType = h.hashType,
                        n = duplicateFilesSet.size,
                        dupN = dupN,
                        main = duplicateFilesSet.find { h.main == it.id } ?: duplicateFilesSet.first(),
                        dups = duplicateFilesSet
                    )
                )
            }
        }

        return DuplicateServiceOperationResult(success = true, obj = duplicatesList,
            result = DuplicatesOperationResultType.OBJECTS_FOUNDED)
    }

    override fun updateDuplicates(duplicates: List<DuplicateDto>, profileId: Long): DuplicateServiceOperationResult {
        val notUpdated = mutableSetOf<DuplicateDto>()
        val updated = mutableListOf<DuplicateDto>()

        duplicates.distinct().forEach { d ->
            val updatedHashEntity = dupHashesAdapter.dtoToEntity(d)
            val hashEntityResult = hashesService.getById(d.id)
            val isCanUpdate = hashEntityResult.success && updatedHashEntity != (hashEntityResult.obj as HashesEntity)
            var logMsg: String? = null

            if (hashEntityResult.success && isCanUpdate) {
                val notFoundedFiles = getNotFoundedFiles(updatedHashEntity.duplicates)
                if (notFoundedFiles.isEmpty()) {
                    d.dups?.forEach { dupFile ->
                        val file = (filesService.getFile(dupFile.id).obj as FilesEntity)
                        filesService.updateFile(file.copy(state = dupFile.state))
                    }

                    val updHashEntityResult = hashesService.update(updatedHashEntity)
                    if (updHashEntityResult.success) {
                        updated.add(d)
                    } else {
                        logMsg = "${getHashDetails(d)} not updated!"
                    }
                } else {
                    logMsg = "${getHashDetails(d)} not updated, cuz next files ${notFoundedFiles.joinToString()} not founded!"
                }
            } else {
                logMsg = "${getHashDetails(d)} not ${"updated".takeIf { hashEntityResult.success } ?: "founded"}!"
            }

            logMsg?.let {
                LOG.warn { logMsg }
                notUpdated.add(d)
            }
        }

        return when {
            updated.isEmpty() -> DuplicateServiceOperationResult(
                success = false, error = "No duplicates updated", result = DuplicatesOperationResultType.OBJECTS_NOT_UPDATED)

            notUpdated.isNotEmpty() -> DuplicateServiceOperationResult(
                success = true, obj = updated, error = getErrorMsg(notUpdated), result = DuplicatesOperationResultType.OBJECTS_UPDATED_PARTLY)

            else -> DuplicateServiceOperationResult(
                success = true, obj = updated, result = DuplicatesOperationResultType.OBJECTS_UPDATED)
        }
    }

    private fun getNotFoundedFiles(ids: Set<Long>): Set<Long> {
        val foundedFiles = filesService.getFiles(ids)

        return when (filesService.getFiles(ids).result) {
            EntityOperationResultType.ENTITIES_FOUNDED_PARTLY ->
                ids - (foundedFiles.obj as List<FilesEntity>).map { it.id }.toSet()

            EntityOperationResultType.ENTITIES_FOUNDED ->
                emptySet()

            else -> ids
        }
    }

    private fun getHashDetails(dto: DuplicateDto): String = "HashesEntity (id:${dto.id} hash:${dto.hash} type:${dto.hashType})"

    private fun getErrorMsg(notUpdated: Set<DuplicateDto>): String =
        if (notUpdated.isEmpty()) "" else "Not updated hashes: " + notUpdated.map { it.id }.joinToString(", ")

}