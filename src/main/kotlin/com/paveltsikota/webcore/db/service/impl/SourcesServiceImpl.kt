package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.constants.DbConst
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_BY_PROFILE_AND_PATH
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_SOURCES
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_SOURCES_BY_PROFILE
import com.paveltsikota.webcore.db.dao.SourcesDao
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.db.service.SourcesService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.rest.api.SourcesDto
import com.paveltsikota.webcore.utils.FileUtils
import com.paveltsikota.webcore.utils.SourcesEntityUtils
import com.paveltsikota.webcore.utils.SourcesEntityUtils.eq
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class SourcesServiceImpl(private val sourcesDao: SourcesDao): SourcesService {

    override fun getSource(id: Long): EntityOperationResult {
        return when (val entity = sourcesDao.findById(id)) {
            null -> EntityOperationResult(success = false, error = "Source entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
            else -> EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    override fun getSources(page: Int?, pageSize: Int?, profileId: Long?): EntityOperationResult {
        val sql = SQL_GET_SOURCES.takeIf { profileId == null }
            ?: SQL_GET_SOURCES_BY_PROFILE

        val params = emptyMap<String, Any>().takeIf { profileId == null }
            ?: mapOf(Pair("profile", profileId!!))

        val ps = DbConst.MAX_PAGE_SIZE.takeIf { pageSize == null || pageSize < 1}
            ?: pageSize


        val result = ArrayList<SourcesEntity>()

        if (page == null || page < 1) {
            var pageResult: List<SourcesEntity>

            var p = 0
            do {
                pageResult = getBySql(sql, params, p++, ps)?: emptyList()
                result.addAll(pageResult)
            } while (pageResult.isEmpty())
        } else {
            result.addAll(getBySql(sql, params, page, ps)?: emptyList())
        }

        return when {
            result.isEmpty() -> EntityOperationResult(
                success = false, error = "Entities not found", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            else -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun addSource(path: Path, profileId: Long, dirorder: Int, addOnce: Boolean?): EntityOperationResult {
        val source = SourcesEntity(path = FileUtils.toUnixPath(path), profile = profileId, dirorder = dirorder)

        return when (addOnce?: true && isAlreadyExist(source)) {
            true -> EntityOperationResult(
                success = false, error = "Entity already exist", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)

            false -> {
                sourcesDao.save(source)
                EntityOperationResult(
                    success = true, obj = source, result = EntityOperationResultType.ENTITY_ALREADY_EXIST)
            }
        }
    }

    override fun updateSource(source: SourcesEntity): EntityOperationResult {
        val result = sourcesDao.update(source)

        return when (eq(result, source)) {
            false -> EntityOperationResult(
                success = false, error = "Entity not updated", obj = source, result = EntityOperationResultType.ENTITY_NOT_UPDATED)

            true -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITY_NOT_UPDATED)
        }
    }

    override fun updateSources(sources: Collection<SourcesEntity>): EntityOperationResult {
        val sourcesSet = sources.toSet()
        val results = sourcesSet.map { updateSource(it) }
        val isAnySuccess = results.parallelStream().anyMatch { it.success }
        val errors = StringBuilder()
        val addedObjects = ArrayList<FilesEntity>()

        results.forEach{
            if (!it.success && it.obj != null) {
                errors.append("not add source '${(it.obj as SourcesEntity).path}'\n")
            } else if (it.success) {
                addedObjects.add(it.obj as FilesEntity)
            }
        }

        val result = if (addedObjects.size == sourcesSet.size) {
            EntityOperationResultType.ENTITIES_ADDED
        } else if (addedObjects.size > 0 && addedObjects.size < sourcesSet.size) {
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

    override fun updateSources(sources: Collection<SourcesDto>): EntityOperationResult {
        val sourceEntities = sources.parallelStream().map { SourcesEntityUtils.dtoToEntity(it) }.distinct().toList()
        return updateSources(sourceEntities)
    }

    override fun removeSource(id: Long): EntityOperationResult {
        return when (sourcesDao.removeById(id)) {
            false -> EntityOperationResult(
                success = false, error = "Entity $id not removed", obj = id, result = EntityOperationResultType.ENTITY_NOT_REMOVED)

            true -> EntityOperationResult(
                success = true, obj = id, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun removeSource(source: SourcesEntity): EntityOperationResult {
        return when (sourcesDao.removeById(source.id)) {
            false -> EntityOperationResult(
                success = false, error = "Entity not removed", obj = source, result = EntityOperationResultType.ENTITY_NOT_REMOVED)

            true -> EntityOperationResult(
                success = true, obj = source.id, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun removeSources(sources: Collection<SourcesEntity>): EntityOperationResult {
        val sourcesSet = sources.toSet()
        val results = sourcesSet.map { removeSource(it.id) }
        val removed = HashSet<Long>()
        val notRemoved = HashSet<Long>()

        results.forEach{
            if (!it.success && it.obj != null) {
                notRemoved.add(it.obj as Long)
            } else if (it.success && it.obj != null) {
                removed.add(it.obj as Long)
            }
        }

        val resultOperation = if (removed.size == sourcesSet.size) {
            EntityOperationResultType.ENTITIES_REMOVED
        } else if (removed.size > 0 && removed.size < sourcesSet.size) {
            EntityOperationResultType.ENTITIES_REMOVED_PARTLY
        } else {
            EntityOperationResultType.ENTITIES_NOT_REMOVED
        }

        return EntityOperationResult(
            success = removed.isNotEmpty(),
            obj = removed,
            error = "Not deleted entities: ${notRemoved.joinToString(", ")}".takeIf { notRemoved.isNotEmpty() },
            result = resultOperation
        )
    }

    override fun removeSources(sources: Collection<SourcesDto>): EntityOperationResult {
        val sourceEntities = sources.parallelStream().map { SourcesEntityUtils.dtoToEntity(it) }.distinct().toList()
        return removeSources(sourceEntities)
    }

    override fun cleanUp(profileId: Long): EntityOperationResult {
        sourcesDao.removeByProfileId(profileId)
        return EntityOperationResult(success = true, result = EntityOperationResultType.ENTITIES_REMOVED)
    }

    override fun isAlreadyExist(sources: SourcesEntity): Boolean {
        val params = mapOf(Pair("profile", sources.profile), Pair("path", sources.path))
        return getBySql(sql = SQL_GET_BY_PROFILE_AND_PATH, params = params, page = null, pageSize = null) != null
    }

    private fun getBySql(sql: String, params: Map<String, Any>, page: Int?, pageSize: Int?): List<SourcesEntity>? {
        return sourcesDao.getBySql(sql, params, page, pageSize)
    }

}