package com.paveltsikota.webcore.utils.entity

import com.paveltsikota.webcore.db.entity.GroupsEntity
import com.paveltsikota.webcore.db.dto.GroupsDto

object GroupsEntityUtils {

    fun eq(e1: GroupsEntity, e2: GroupsEntity): Boolean {
        return e1.id == e2.id
                && e1.size == e2.size
                && e1.profile == e2.profile
                && e1.fileIds == e2.fileIds
    }

    fun entityToDto(e: GroupsEntity): GroupsDto {
        return GroupsDto(id = e.id, profile = e.profile, size = e.size, fileIds = e.fileIds)
    }

    fun dtoToEntity(dto: GroupsDto): GroupsEntity {
        return with(dto) {
            GroupsEntity(id = dto.id ?: 0, profile = dto.profile, size = dto.size, fileIds = dto.fileIds ?: emptySet())
        }
    }

}