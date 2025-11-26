package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.entity.GroupsEntity
import org.springframework.stereotype.Component

@Component
object GroupsAdapter: AbstractEntityDtoAdapter<GroupsEntity, GroupsDto>(
    entityClass = GroupsEntity::class,
    dtoClass = GroupsDto::class
) {

    override fun entityToDto(e: GroupsEntity): GroupsDto {
        return with(e) { GroupsDto(id, profile, size, fileIds) }
    }

    override fun dtoToEntity(dto: GroupsDto): GroupsEntity {
        return with(dto) { GroupsEntity(id ?: 0, profile, size, fileIds ?: emptySet()) }
    }

}