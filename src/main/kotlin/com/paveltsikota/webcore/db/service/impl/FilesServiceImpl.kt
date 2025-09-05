package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.constants.DbConst
import com.paveltsikota.webcore.db.dao.FilesDao
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.service.FilesService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.hash.calculator.HashCalculator
import com.paveltsikota.webcore.rest.api.FilesDto
import com.paveltsikota.webcore.utils.FilesEntityUtils
import com.paveltsikota.webcore.utils.FilesEntityUtils.eq
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class FilesServiceImpl(private val filesDao: FilesDao): FilesService {
    val SQL_GET_NOT_HASHED = "FROM FilesEntity p WHERE p.profile = :profile AND p.hashId IS NULL"
    val SQL_GET_NOT_HASHED_WITH_SIZE = "FROM FilesEntity p WHERE p.profile = :profile AND p.size = :size AND p.hashId IS NULL"
    val SQL_GET_NOT_GROUPED = "FROM FilesEntity p WHERE p.profile = :profile AND p.groupId IS NULL"
    val SQL_GET_NOT_GROUPED_WITH_SIZE = "FROM FilesEntity p WHERE p.profile = :profile AND p.size = :size AND p.groupId IS NULL"
    val SQL_GET_NOT_HASH_CALCULATED = "FROM FilesEntity p WHERE p.profile = :profile AND p.size = :size AND p.groupId IS NULL AND p.hashId IS NULL AND p.hash IS NULL and p.hashType IS NULL"
    val SQL_GET_NOT_HASH_CALCULATED_WITH_SIZE = "FROM FilesEntity p WHERE p.profile = :profile p.size = :size AND AND p.groupId IS NULL AND p.hashId IS NULL AND p.hash IS NULL and p.hashType IS NULL"

    override fun getFile(id: Long): EntityOperationResult {
        val entity = filesDao.findById(id)

        return if (entity != null) {
            EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        } else {
            EntityOperationResult(success = false, error = "Entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
        }
    }

    override fun getFiles(ids: Collection<Long>): EntityOperationResult {
        val idsList = ids.filter { it > 0 }.distinct()

        val entities = idsList.chunked(DbConst.MAX_PAGE_SIZE)
            .mapNotNull { filesDao.findByIds(it) }
            .filter{ it.isNotEmpty() }
            .flatten()

        return when {
            entities.isNullOrEmpty() -> EntityOperationResult(
                success = false, error = "Entities not found", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            entities.size < idsList.size -> {
                val notFounded = ids.subtract(entities.map { it.id }.toSet()).joinToString(prefix = "'", postfix = "'", separator = ", ")
                EntityOperationResult(
                    success = true, error = "Entities $notFounded not found", obj = entities, result = EntityOperationResultType.ENTITIES_FOUNDED_PARTLY)
            }

            else -> EntityOperationResult(
                success = true, obj = entities, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun getFiles(page: Int, pageSize: Int, profileId: Long?): EntityOperationResult {
        val entities = filesDao.getFiles(page, pageSize, profileId)
        return when {
            entities.isNullOrEmpty() -> EntityOperationResult(
                success = false, error = "Entities not found", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            else -> EntityOperationResult(
                success = true, obj = entities, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun addLocalFile(filePath: Path, calc: HashCalculator?, addOnce: Boolean?): EntityOperationResult {
        val entity = FilesEntityUtils.getFilesEntryByPathForDb(filePath, calc)

        return when {
            entity.state == FileState.NOT_FOUND -> EntityOperationResult(
                success = false, obj = entity, error = "File not found", result = EntityOperationResultType.ENTITY_NOT_ADD)

            addOnce == true && isAlreadyExist(entity) -> EntityOperationResult(
                success = false, obj = entity, error = "File already exist in db", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)

            else -> {
                filesDao.save(entity)
                EntityOperationResult(
                    success = true, obj = entity, result = EntityOperationResultType.ENTITY_ADD)
            }
        }
    }

    override fun addRemoteFile(fileDto: FilesDto, addOnce: Boolean?): EntityOperationResult {
        val entity = FilesEntityUtils.dtoToEntity(dto = fileDto, isLocal = false)
        return when {
            addOnce == true && isAlreadyExist(entity) -> EntityOperationResult(
                success = false, obj = entity, error = "File already exist in db", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)

            else -> {
                filesDao.save(entity)
                EntityOperationResult(
                    success = true, obj = entity, result = EntityOperationResultType.ENTITY_ADD)
            }
        }
    }

    override fun addRemoteFiles(fileDto: List<FilesDto>, addOnce: Boolean?): EntityOperationResult {
        val results = fileDto.map { addRemoteFile(it, addOnce) }
        val isAnySuccess = results.parallelStream().anyMatch { it.success }
        val errors = StringBuilder()
        val addedObjects = ArrayList<FilesEntity>()

        results.forEach{
            if (!it.success && it.obj != null) {
                errors.append("not add file '${(it.obj as FilesEntity)}'\n")
            } else if (it.success) {
                addedObjects.add(it.obj as FilesEntity)
            }
        }

        val result = if (addedObjects.size == fileDto.size) {
            EntityOperationResultType.ENTITIES_ADDED
        } else if (addedObjects.size > 0 && addedObjects.size < fileDto.size) {
            EntityOperationResultType.ENTITIES_ADDED_PARTLY
        } else {
            EntityOperationResultType.ENTITIES_NOT_ADDED
        }

        return EntityOperationResult(
            success = isAnySuccess,
            obj = addedObjects,
            error = errors.toString(),
            result = result
        )
    }

    override fun updateFile(file: FilesEntity): EntityOperationResult {
        val obj = filesDao.update(file)
        return if (eq(file, obj)) {
            EntityOperationResult(success = true, obj = obj, result = EntityOperationResultType.ENTITY_UPDATED)
        } else {
            EntityOperationResult(success = false, obj = obj, result = EntityOperationResultType.ENTITY_NOT_UPDATED)
        }
    }

    override fun updateFile(fileFto: FilesDto, isLocal: Boolean?): EntityOperationResult {
        return updateFile(FilesEntityUtils.dtoToEntity(fileFto, isLocal ?: false))
    }

    override fun removeFile(file: FilesEntity): EntityOperationResult {
        return removeFile(file.id)
    }

    override fun removeFile(fileDto: FilesDto): EntityOperationResult {
        return if (fileDto.id == null || fileDto.id <= 0) {
            EntityOperationResult(success = false, error = "id is null or has false value", result = EntityOperationResultType.ENTITY_NOT_REMOVED)
        } else {
            removeFile(fileDto.id)
        }
    }

    override fun removeFile(id: Long): EntityOperationResult {
        return if (filesDao.removeById(id)) {
            EntityOperationResult(success = true, result = EntityOperationResultType.ENTITY_REMOVED)
        } else {
            EntityOperationResult(success = false, error = "Entity '$id' not found", obj = id, result = EntityOperationResultType.ENTITY_NOT_REMOVED)
        }
    }

    override fun removeFiles(ids: Collection<Long>): EntityOperationResult {
        val idsSet = ids.toSet()
        val results = idsSet.map { removeFile(it) }
        val notRemoved = results.filter { !it.success }.map { it.obj as Long }

        return when {
            notRemoved.size == idsSet.size -> EntityOperationResult(
                success = false, error = "Entities ${notRemoved.joinToString(prefix = "'", postfix = "'", separator = ", ")} not removed", result = EntityOperationResultType.ENTITIES_NOT_REMOVED)

            notRemoved.isNotEmpty() -> EntityOperationResult(
                success = true, error = "Entities ${notRemoved.joinToString(prefix = "'", postfix = "'", separator = ", ")} not removed", result = EntityOperationResultType.ENTITIES_REMOVED_PARTLY)

            else -> EntityOperationResult(
                success = true, result = EntityOperationResultType.ENTITIES_REMOVED)
        }
    }

    override fun findByGroupId(groupId: Long, profileId: Long): EntityOperationResult {
        val result = filesDao.findByGroupId(groupId, profileId)

        return when {
            result.isNullOrEmpty() -> EntityOperationResult(
                success = false, error = "Entities not found", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            else -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun findByHashId(hashId: Long, profileId: Long): EntityOperationResult {
        val result = filesDao.findByHashId(hashId, profileId)

        return when {
            result.isNullOrEmpty() -> EntityOperationResult(
                success = false, error = "Entities not found", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            else -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun findByHash(hash: String, hashType: HashType, profileId: Long): EntityOperationResult {
        val result = filesDao.findByHash(hash, hashType, profileId)

        return when {
            result.isNullOrEmpty() -> EntityOperationResult(
                success = false, error = "Entities not found", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            else -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun findBySize(page: Int?, pageSize: Int?, size: Long, profileId: Long): EntityOperationResult {
        val result = filesDao.findBySize(page, pageSize, size, profileId)

        return when {
            result.isNullOrEmpty() -> EntityOperationResult(
                success = false, error = "Entities not found", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            else -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    // TODO: optimize it!!!
    override fun findNotGrouped(profileId: Long, size: Long?, page: Int, pageSize: Int): List<FilesEntity> {
        val sql = SQL_GET_NOT_GROUPED_WITH_SIZE.takeIf { size != null } ?: SQL_GET_NOT_GROUPED
        val params = getProfileAndSizeMap(profileId, size)

        return filesDao.getBySql(sql, params, page, pageSize)?: emptyList()
    }

    override fun findAllNotGrouped(profileId: Long, size: Long?): List<FilesEntity> {
        val result = ArrayList<FilesEntity>()
        var partResult: List<FilesEntity>
        var p = 0
        do {
            partResult = findNotGrouped(profileId, size, p++, DbConst.MAX_PAGE_SIZE)
            result.addAll(partResult)
        } while (partResult.isEmpty())

        return result
    }

    // TODO: optimize it!!!
    override fun findNotHashed(profileId: Long, size: Long?, page: Int, pageSize: Int): List<FilesEntity> {
        val sql = SQL_GET_NOT_HASHED_WITH_SIZE.takeIf { size != null } ?: SQL_GET_NOT_HASHED
        val params = getProfileAndSizeMap(profileId, size)

        return filesDao.getBySql(sql, params, page, pageSize)?: emptyList()
    }

    override fun findAllNotHashed(profileId: Long, size: Long?): List<FilesEntity> {
        val result = ArrayList<FilesEntity>()
        var partResult: List<FilesEntity>
        var p = 0
        do {
            partResult = findNotHashed(profileId, size, p++, DbConst.MAX_PAGE_SIZE)
            result.addAll(partResult)
        } while (partResult.isEmpty())

        return result
    }

    // TODO: optimize it!!!
    override fun findNotCalculatedHash(profileId: Long, size: Long?, page: Int, pageSize: Int): List<FilesEntity> {
        val sql = SQL_GET_NOT_HASH_CALCULATED_WITH_SIZE.takeIf { size != null } ?: SQL_GET_NOT_HASH_CALCULATED
        val params = getProfileAndSizeMap(profileId, size)

        return filesDao.getBySql(sql, params, page, pageSize)?: emptyList()
    }

    override fun findAllNotCalculatedHash(profileId: Long, size: Long?): List<FilesEntity> {
        val result = ArrayList<FilesEntity>()
        var partResult: List<FilesEntity>
        var p = 0
        do {
            partResult = findNotCalculatedHash(profileId, size, p++, DbConst.MAX_PAGE_SIZE)
            result.addAll(partResult)
        } while (partResult.isEmpty())

        return result
    }

    override fun isAlreadyExist(entity: FilesEntity): Boolean {
        return filesDao.checkAlreadyExistEntity(Path.of(entity.path), entity.size, entity.hash, entity.hashType, entity.profile)
    }

    private fun getProfileAndSizeMap(profileId: Long, size: Long?): Map<String, Any> {
        val map = mutableMapOf(Pair("profile", profileId))
        if (size != null) {
            map["size"] = size
        }

        return map
    }
}