package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.entity.GroupsEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult

interface GroupsService {
    fun getGroup(id: Long): EntityOperationResult
    fun getGroups(page: Int?, pageSize: Int?, profileId: Long?): EntityOperationResult
    fun getAllGroups(profileId: Long?): EntityOperationResult

    fun addGroup(size: Long, profileId: Long?, fileIds: Collection<Long>, addOnce: Boolean?): EntityOperationResult

    fun updateGroup(group: GroupsEntity): EntityOperationResult

    fun removeGroup(group: GroupsEntity): EntityOperationResult
    fun removeGroup(id: Long): EntityOperationResult

    fun findBySize(size: Long, profileId: Long): EntityOperationResult

    fun cleanUp(profileId: Long): EntityOperationResult

    fun isAlreadyExist(group: GroupsEntity): Boolean
}