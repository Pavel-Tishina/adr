package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.constants.DbConst
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_HASHES
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_HASHES_BY_N
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_HASHES_BY_PROFILE
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_HASHES_BY_PROFILE_AND_N
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_HASHES_BY_PROFILE_AND_SIZE
import com.paveltsikota.webcore.db.constants.DbConst.SQL_HASHES_ALREADY_EXIST
import com.paveltsikota.webcore.db.dao.HashesDao
import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.service.HashesService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.utils.entity.HashesEntityUtils.eq
import com.paveltsikota.webcore.utils.entity.HashesEntityUtils.setUpdate
import com.paveltsikota.webcore.utils.enums.HashType
import org.springframework.stereotype.Service

@Service
class HashesServiceImpl(private val hashesDao: HashesDao): HashesService {
    override fun getById(id: Long): EntityOperationResult {
        return when (val entity = hashesDao.findById(id)) {
            null -> EntityOperationResult(success = false, error = "Entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
            else -> EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    override fun getByIds(ids: Collection<Long>): EntityOperationResult {
        val idsList = ids.filter { it > 0 }.distinct()

        val entities = idsList.chunked(DbConst.MAX_PAGE_SIZE)
            .mapNotNull { hashesDao.findByIds(it) }
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

    override fun getAll(page: Int?, pageSize: Int?, profileId: Long?): EntityOperationResult {
        return getAllByN(page = page, pageSize = pageSize, profileId = profileId, n = null)
    }

    override fun getAllByN(page: Int?, pageSize: Int?, profileId: Long?, n: Int?): EntityOperationResult {
        val sql = when {
            profileId == null && n == null -> SQL_GET_HASHES
            profileId == null -> SQL_GET_HASHES_BY_N
            n == null -> SQL_GET_HASHES_BY_PROFILE
            else -> SQL_GET_HASHES_BY_PROFILE_AND_N
        }

        val params = getParamsProfileAndN(profileId, n)
        val result = executeGetBySql(sql = sql, params = params, page = page, pageSize = pageSize)

        return when (result.isNullOrEmpty()) {
            true -> EntityOperationResult(
                success = false, error = "Entities not found", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            false -> EntityOperationResult(
                success = false, error = "Entities not found", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)
        }
    }

    override fun add(
        profileId: Long,
        size: Long,
        hash: String,
        hashType: HashType,
        main: Long,
        dupIds: Collection<Long>?,
        addOnce: Boolean?
    ): EntityOperationResult {
        val newEntity = HashesEntity(size = size, profile = profileId, hash = hash, hashType = hashType, main = main, duplicates = (dupIds?.toSet() ?: mutableSetOf()) as MutableSet)

        return if (addOnce == true && isAlreadyExist(newEntity)) {
            EntityOperationResult(success = false, error = "Entity already exist", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)
        } else {
            hashesDao.save(newEntity)
            EntityOperationResult(success = true, obj = newEntity, result = EntityOperationResultType.ENTITY_ADD)
        }
    }

    override fun update(entity: HashesEntity): EntityOperationResult {
        val obj = hashesDao.update(entity)
        return if (eq(entity, obj)) {
            EntityOperationResult(success = true, obj = obj, result = EntityOperationResultType.ENTITY_UPDATED)
        } else {
            EntityOperationResult(success = false, obj = obj, result = EntityOperationResultType.ENTITY_NOT_UPDATED)
        }
    }

    override fun update(id: Long, profileId: Long, main: Long, dupIds: Collection<Long>?): EntityOperationResult {
        val entity = hashesDao.findById(id)

        return if (entity == null || entity.profile != profileId) {
            EntityOperationResult(success = false, error = "Entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
        } else {
            val updatedEntity = setUpdate(entity, main, dupIds?: emptySet())
            update(updatedEntity)
        }
    }

    override fun remove(id: Long): EntityOperationResult {
        return when (hashesDao.removeById(id)) {
            false -> EntityOperationResult(success = false, error = "Entity not removed", result = EntityOperationResultType.ENTITY_NOT_REMOVED)
            true -> EntityOperationResult(success = true, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun remove(entity: HashesEntity): EntityOperationResult {
        return remove(entity.id)
    }

    override fun findByHash(hash: String, hashType: HashType, profileId: Long): EntityOperationResult {
        val result = hashesDao.findByHashAndProfileId(hash, hashType, profileId)
        return when {
            result == null -> EntityOperationResult(
                success = false, error = "Entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)

            else -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    override fun findBySize(page: Int?, pageSize: Int?, size: Long, profileId: Long): EntityOperationResult {
        val params = mapOf(
            Pair("profile", profileId),
            Pair("size", size)
        )

        val result = executeGetBySql(sql = SQL_GET_HASHES_BY_PROFILE_AND_SIZE, params = params, page = page, pageSize = pageSize)

        return when {
            result.isNullOrEmpty() -> EntityOperationResult(
                success = false, error = "Entities not founded", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            else -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun getBySql(sql: String, params: Map<String, Any>, page: Int?, pageSize: Int?): List<HashesEntity>? {
        return hashesDao.getBySql(sql = sql, params = params, page = page, pageSize = pageSize)
    }

    override fun cleanUp(profileId: Long): EntityOperationResult {
        var count = 0
        var page = 0
        val notDeleted = HashSet<Long>()
        do {
            val partResult = getAll(page = ++page, pageSize = DbConst.MAX_PAGE_SIZE, profileId = profileId)

            if (partResult.success && (partResult.obj as List<*>).isNotEmpty()) {
                partResult.obj.forEach { if (!hashesDao.removeById((it as HashesEntity).id)) notDeleted.add(it.id) }
                count += partResult.obj.size
            }
        } while (partResult.success)

        return when {
            count == 0 -> EntityOperationResult(
                success = false, error = "There is no entities by profile '$profileId'",
                result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            notDeleted.isNotEmpty() -> EntityOperationResult(
                success = true, error = "Next entities not deleted ${notDeleted.joinToString(separator = ", ")}", obj = count,
                result = EntityOperationResultType.ENTITIES_REMOVED_PARTLY)

            else -> EntityOperationResult(
                success = true, obj = count,
                result = EntityOperationResultType.ENTITIES_REMOVED)
        }
    }

    override fun isAlreadyExist(e: HashesEntity): Boolean {
        val params = with(e) {
            mapOf(
                Pair("size", size),
                Pair("profile", profile),
                Pair("hash", hash),
                Pair("hashType", hashType),
            )
        }

//        return !executeGetBySql(sql = SQL_HASHES_ALREADY_EXIST, params = params).isNullOrEmpty()
        return !hashesDao.getBySql(sql = SQL_HASHES_ALREADY_EXIST, params = params).isNullOrEmpty()
    }



    private fun getParamsProfileAndN(profileId: Long? = null, n: Int? = null): Map<String, Any> {
        val map = hashMapOf<String, Any>()
        profileId?.let { map["profile"] = profileId }
        n?.let { map["n"] = n }

        return map
    }

    private fun executeGetBySql(sql: String, params: Map<String, Any>, page: Int? = null, pageSize: Int? = null): List<HashesEntity>? {
        if (sql.isBlank()) {
            return null
        }

        val ps = DbConst.MAX_PAGE_SIZE.takeIf { pageSize == null || pageSize < 1} ?: pageSize

        return if (page == null || page < 1) {
            val result = ArrayList<HashesEntity>()
            var pageResult: List<HashesEntity>

            var p = 0
            do {
                pageResult = getBySql(sql, params, ++p, ps) ?: emptyList()
                result.addAll(pageResult)
            } while (pageResult.isNotEmpty())

            result
        } else {
            getBySql(sql, params, page, ps)
        }
    }

}