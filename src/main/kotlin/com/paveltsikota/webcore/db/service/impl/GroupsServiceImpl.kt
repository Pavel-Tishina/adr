package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.constants.DbConst
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_GROUPS
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_GROUPS_BY_PROFILE
import com.paveltsikota.webcore.db.dao.GroupsDao
import com.paveltsikota.webcore.db.entity.GroupsEntity
import com.paveltsikota.webcore.db.service.GroupsService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.utils.entity.GroupsEntityUtils.eq
import org.springframework.stereotype.Service

@Service
class GroupsServiceImpl(private val groupsDao: GroupsDao): GroupsService {
    override fun getGroup(id: Long): EntityOperationResult {
        return when (val entity = groupsDao.findById(id)) {
            null -> EntityOperationResult(success = false, error = "Entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
            else -> EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    // TODO: bloody hell!!!
    override fun getGroups(page: Int?, pageSize: Int?, profileId: Long?): EntityOperationResult {
        val sql = SQL_GET_GROUPS.takeIf { profileId == null }
            ?: SQL_GET_GROUPS_BY_PROFILE

        val params = emptyMap<String, Any>().takeIf { profileId == null }
            ?: mapOf(Pair("profile", profileId!!))

        val ps = DbConst.MAX_PAGE_SIZE.takeIf { pageSize == null || pageSize < 1}
            ?: pageSize


        val result = ArrayList<GroupsEntity>()

        if (page == null || page < 1) {
            var pageResult: List<GroupsEntity>

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

    override fun getAllGroups(profileId: Long?): EntityOperationResult {
        return getGroups(page = null, pageSize = null, profileId = profileId)
    }

    override fun addGroup(size: Long, profileId: Long?, fileIds: Collection<Long>, addOnce: Boolean?): EntityOperationResult {
        val group = GroupsEntity(size = size, profile = profileId?: 0, fileIds = fileIds.toSet())

        return when (addOnce?: true && isAlreadyExist(group)) {
            true -> EntityOperationResult(
                success = false, error = "Entity already exist", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)

            false -> {
                groupsDao.save(group)
                EntityOperationResult(success = true, obj = group, result = EntityOperationResultType.ENTITY_ADD)
            }

        }
    }

    override fun updateGroup(group: GroupsEntity): EntityOperationResult {
        val result = groupsDao.update(group)

        return when (eq(result, group)) {
            false -> EntityOperationResult(
                success = false, error = "Entity not updated", obj = group, result = EntityOperationResultType.ENTITY_NOT_UPDATED)

            true -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITY_NOT_UPDATED)
        }
    }

    override fun removeGroup(group: GroupsEntity): EntityOperationResult {
        return removeGroup(group.id)
    }

    override fun removeGroup(id: Long): EntityOperationResult {
        return when (groupsDao.removeById(id)) {
            false -> EntityOperationResult(
                success = false, error = "Group entity $id not removed", result = EntityOperationResultType.ENTITY_NOT_REMOVED)

            true -> EntityOperationResult(
                success = true, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun findBySize(size: Long, profileId: Long): EntityOperationResult {
        val entity = groupsDao.findBySizeAndProfileId(size, profileId)?: emptyList()

        return when {
            entity.isEmpty() -> EntityOperationResult(
                success = false, error = "Group entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)

            entity.size > 1 -> EntityOperationResult(
                success = true,
                error = "Much more 1 entity was founded! Ids: ${entity.map { it.id }.joinToString(separator = ", ")}",
                obj = entity,
                result = EntityOperationResultType.ENTITIES_FOUNDED
            )

            else -> EntityOperationResult(success = true, obj = entity[0], result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    override fun cleanUp(profileId: Long): EntityOperationResult {
        var count = 0
        var page = 0
        var partResult: List<GroupsEntity>
        val notDeleted = HashSet<Long>()
        val params = mapOf(Pair("profile", profileId))
        do {
            partResult = getBySql(sql = SQL_GET_GROUPS_BY_PROFILE, page = page++, pageSize = DbConst.MAX_PAGE_SIZE, params = params)?: emptyList()
            count += partResult.size

            if (partResult.isNotEmpty()) {
                partResult.forEach { if (!groupsDao.removeById(it.id)) { notDeleted.add(it.id)} }
            }
        } while (partResult.isEmpty())

        return when {
            count == 0 -> EntityOperationResult(
                success = false, error = "There is no groups entities by profile '$profileId'",
                result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            notDeleted.isNotEmpty() -> EntityOperationResult(
                success = true, error = "Next groups entities wasn't deleted ${notDeleted.joinToString(separator = ", ")}", obj = count,
                result = EntityOperationResultType.ENTITIES_REMOVED_PARTLY)

            else -> EntityOperationResult(
                success = true, obj = count,
                result = EntityOperationResultType.ENTITIES_REMOVED)
        }
    }

    override fun isAlreadyExist(group: GroupsEntity): Boolean {
        return groupsDao.findBySizeAndProfileId(group.size, group.profile) != null
    }

    private fun getBySql(sql: String, params: Map<String, Any>, page: Int?, pageSize: Int?): List<GroupsEntity>? {
        return groupsDao.getBySql(sql, params, page, pageSize)
    }
}