package com.paveltsikota.webcore.db.service.impl

import aj.org.objectweb.asm.commons.AdviceAdapter
import com.paveltsikota.webcore.db.adapter.DuplicateFileAdapter
import com.paveltsikota.webcore.db.adapter.DuplicateHashAdapter
import com.paveltsikota.webcore.db.adapter.HashesAdapter
import com.paveltsikota.webcore.db.dto.DuplicateDto
import com.paveltsikota.webcore.db.dto.DuplicateFileDto
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.service.DuplicateService
import com.paveltsikota.webcore.db.service.FilesService
import com.paveltsikota.webcore.db.service.HashesService
import com.paveltsikota.webcore.db.service.result.DuplicateServiceOperationResult
import com.paveltsikota.webcore.db.service.result.enums.DuplicatesOperationResultType
import org.springframework.stereotype.Service
import kotlin.Long

@Service
class DuplicateServiceImpl(
    private val filesService: FilesService,
    private val hashesService: HashesService,
    private val dupFilesAdapter: DuplicateFileAdapter,
    private val dupHashesAdapter: DuplicateHashAdapter,
    private val hashesAdapter: HashesAdapter,
): DuplicateService {

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
        duplicates.distinct().forEach { d ->
            val hashEntityResult = hashesService.getById(d.id)

            if (hashEntityResult.success) {
                val updatedHashEntity = dupHashesAdapter.dtoToEntity(d)
                val existedHashEntity = hashEntityResult.obj as HashesEntity

                if (existedHashEntity != updatedHashEntity) {
                    hashesService.update(updatedHashEntity)
                    //!!
                }
            }

        }
        return DuplicateServiceOperationResult(success = true, obj = duplicates,)
    }

    private fun isUpdate(existed: HashesEntity, updated: HashesEntity): Boolean {
//        return with(existed) {
//            id == updated.id,
//
//        }
        return false
    }


}