package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.adapter.SourcesAdapter
import com.paveltsikota.webcore.db.constants.DbConst
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_BY_PROFILE_AND_PATH
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_SOURCES
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_SOURCES_BY_PROFILE
import com.paveltsikota.webcore.db.dao.SourcesDao
import com.paveltsikota.webcore.db.dto.SourcesDto
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.db.service.SourcesService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.utils.FileUtils
import com.paveltsikota.webcore.utils.ValuesUtils.isMoreThanZero
import com.paveltsikota.webcore.utils.ValuesUtils.profileIdChk
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class SourcesServiceImpl(
    private val sourcesDao: SourcesDao
): SourcesService {

    override fun getSource(id: Long): EntityOperationResult {
        return when (val entity = sourcesDao.findById(id)) {
            null -> EntityOperationResult(success = false, error = "Source entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
            else -> EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    override fun getSources(page: Int?, pageSize: Int?, profileId: Long?): EntityOperationResult {
        val sql = SQL_GET_SOURCES.takeIf { !profileIdChk(profileId) }
            ?: SQL_GET_SOURCES_BY_PROFILE

        val params = emptyMap<String, Any>().takeIf { !profileIdChk(profileId) }
            ?: mapOf(Pair("profile", profileId!!))

        val ps = DbConst.MAX_PAGE_SIZE.takeIf { !isMoreThanZero(pageSize) }
            ?: pageSize


        val result = ArrayList<SourcesEntity>()

        if (!isMoreThanZero(page)) {
            var pageResult: List<SourcesEntity>

            var p = 0
            do {
                pageResult = getBySql(sql, params, page = ++p, pageSize = ps)?: emptyList()
                result.addAll(pageResult)
            } while (pageResult.isNotEmpty())
        } else {
            result.addAll(getBySql(sql, params, page, pageSize = ps)?: emptyList())
        }

        return when {
            result.isEmpty() -> EntityOperationResult(
                success = false, error = "Entities not found", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            else -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun getSources(ids: Collection<Long>): EntityOperationResult {
        val listIds = ids.filter{ it > 0 }.distinct().toList()
        if (listIds.isNullOrEmpty())
            return EntityOperationResult(
                success = false, obj = null, error = "Bad ids in input", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

        val entities = listIds.map { sourcesDao.getByIds(listIds) }

        return when {
            entities.isNullOrEmpty() -> EntityOperationResult(
                success = false, obj = null, result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            entities.size < listIds.size -> EntityOperationResult(
                success = true, obj = entities, result = EntityOperationResultType.ENTITIES_FOUNDED_PARTLY)

            else -> EntityOperationResult(
                success = true, obj = entities, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }

    }

    override fun addSource(path: Path, profileId: Long, dirorder: Int, addOnce: Boolean?): EntityOperationResult {
        val source = SourcesEntity(path = FileUtils.toUnixPath(path), profile = profileId, dirorder = dirorder)

        return when {
            !profileIdChk(profileId) -> EntityOperationResult(
                success = false, error = "ProfileId value is wrong", result = EntityOperationResultType.ENTITY_NOT_ADD)

            addOnce != false && isAlreadyExist(source, true) -> EntityOperationResult(
                success = false, error = "Entity already exist", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)

            else -> {
                sourcesDao.save(source)
                EntityOperationResult(
                    success = true, obj = source, result = EntityOperationResultType.ENTITY_ADD)
            }
        }
    }

    override fun addSourcesDto(
        sources: Collection<SourcesDto>,
        addOnce: Boolean?
    ): EntityOperationResult {
        val sourcesSet = sources.distinct()
        val added = mutableSetOf<SourcesEntity>()
        val errors = mutableSetOf<SourcesDto>()

        sourcesSet.forEach { dto ->
            val resultOp = with(dto) {
                addSource(Path.of(path), profile, dirorder, addOnce)
            }
            if (resultOp.success) {
                added.add(resultOp.obj as SourcesEntity)
            } else {
                errors.add(dto)
            }
        }

        return when {
            added.isEmpty() -> EntityOperationResult(
                success = false, error = "Entities not added: ${errorPathesMsg(errors)}", result = EntityOperationResultType.ENTITIES_NOT_ADDED)

            errors.isNotEmpty() -> EntityOperationResult(
                success = true, obj = added, error = "Entities not added: ${errorPathesMsg(errors)}", result = EntityOperationResultType.ENTITIES_ADDED_PARTLY)

            else -> EntityOperationResult(
                success = true, obj = added, result = EntityOperationResultType.ENTITIES_ADDED)
        }
    }

    // TODO: dirty }}}}}
    override fun updateSource(source: SourcesEntity): EntityOperationResult {
        return when(isAlreadyExist(source, false)) {
            false -> EntityOperationResult(
                success = false, error = "Entity not found", obj = source, result = EntityOperationResultType.ENTITY_NOT_FOUND)

            else -> {
                val existed = sourcesDao.findById(source.id)
                when {
                    existed != source -> EntityOperationResult(
                        success = false, error = "Entity not updated", obj = source, result = EntityOperationResultType.ENTITY_NOT_UPDATED)

                    else -> {
                        val result = sourcesDao.update(source)
                        EntityOperationResult(
                            success = true, obj = result, result = EntityOperationResultType.ENTITY_UPDATED)
                    }
                }
            }
        }
    }

    override fun updateSources(sources: Collection<SourcesEntity>): EntityOperationResult {
        val sourcesSet = sources.toSet()
        val results = sourcesSet.map { updateSource(it) }
        val isAnySuccess = results.parallelStream().anyMatch { it.success }
        val errors = HashSet<SourcesEntity>()
        val addedObjects = ArrayList<SourcesEntity>()

        results.forEach{
            if (!it.success && it.obj != null) {
                errors.add(it.obj as SourcesEntity)
            } else if (it.success) {
                addedObjects.add(it.obj as SourcesEntity)
            }
        }

        val result = if (addedObjects.size == sourcesSet.size) {
            EntityOperationResultType.ENTITIES_ADDED
        } else if (addedObjects.isNotEmpty() && addedObjects.size < sourcesSet.size) {
            EntityOperationResultType.ENTITIES_ADDED_PARTLY
        } else {
            EntityOperationResultType.ENTITIES_NOT_ADDED
        }

        return EntityOperationResult(
            success = isAnySuccess,
            obj = addedObjects,
            error = "Not updated: ${errorPathesMsg(errors.map(SourcesAdapter::entityToDto))}"
                .takeIf { errors.isNotEmpty() } ?: "",
            result = result
        )
    }

    override fun updateSourcesDto(sources: Collection<SourcesDto>): EntityOperationResult {
        val sourceEntities = sources.distinct().parallelStream().map(SourcesAdapter::dtoToEntity).toList()
        return updateSources(sourceEntities)
    }

    // TODO: check profile too
    override fun removeSource(id: Long): EntityOperationResult {
        return when (sourcesDao.removeById(id)) {
            false -> EntityOperationResult(
                success = false, error = "Entity $id not removed", obj = id, result = EntityOperationResultType.ENTITY_NOT_REMOVED)

            true -> EntityOperationResult(
                success = true, obj = id, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun removeSource(source: SourcesEntity): EntityOperationResult {
        return when (sourcesDao.removeByIdAndProfile(source.id, source.profile)) {
            false -> EntityOperationResult(
                success = false, error = "Entity not removed", obj = source, result = EntityOperationResultType.ENTITY_NOT_REMOVED)

            true -> EntityOperationResult(
                success = true, obj = source.id, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun removeSources(sources: Collection<SourcesEntity>): EntityOperationResult {
        val sourcesSet = sources.toSet()
        val results = sourcesSet.map { removeSource(it) }
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
        } else if (removed.isNotEmpty() && removed.size < sourcesSet.size) {
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

    override fun removeSourcesDto(sources: Collection<SourcesDto>): EntityOperationResult {
        val sourceEntities = sources.parallelStream().map { SourcesAdapter.dtoToEntity(it) }.distinct().toList()
        return removeSources(sourceEntities)
    }

    override fun cleanUp(profileId: Long): EntityOperationResult {
        return when (sourcesDao.removeByProfileId(profileId)) {
            0 -> EntityOperationResult(
                success = false, error = "Not found entities by profile $profileId", result = EntityOperationResultType.ENTITIES_NOT_ADDED)

            1 -> EntityOperationResult(
                success = true, result = EntityOperationResultType.ENTITY_REMOVED)

            else -> EntityOperationResult(
                success = true, result = EntityOperationResultType.ENTITIES_REMOVED)
        }
    }

    // TODO: split to isAlreadyExist and canUpdate
    override fun isAlreadyExist(sources: SourcesEntity, isCreate: Boolean): Boolean {
        return if (isCreate) {
            val params = mapOf("profile" to sources.profile, "path" to sources.path)
            getBySql(sql = SQL_GET_BY_PROFILE_AND_PATH, params = params, page = null, pageSize = null)
                ?.isNotEmpty() == true
        } else with(sources) {
            sourcesDao.findByProfileAndId(id, profile) != null
        }
    }

    internal fun getBySql(sql: String, params: Map<String, Any>, page: Int?, pageSize: Int?): List<SourcesEntity>? {
        return sourcesDao.getBySql(sql, params, page, pageSize)
    }

    private fun errorPathesMsg(errors: Collection<SourcesDto>): String {
        return errors
            .distinct()
            .joinToString(", ")
            { "'${it.path}'${ if (it.id != null) { " profile=${it.id}" } else {}}" }
    }

}