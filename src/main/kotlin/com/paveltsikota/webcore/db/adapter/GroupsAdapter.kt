package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.entity.GroupsEntity
import org.springframework.stereotype.Component

@Component
object GroupsAdapter: AbstractEntityDtoAdapter<GroupsEntity, GroupsDto>(
    entityClass = GroupsEntity::class.java,
    dtoClass = GroupsDto::class.java
) {

    override fun eqEntity(e1: GroupsEntity, e2: GroupsEntity): Boolean {
        return e1.id == e2.id
                && e1.size == e2.size
                && e1.profile == e2.profile
                && e1.fileIds == e2.fileIds
    }

    override fun eqDto(dto1: GroupsDto, dto2: GroupsDto): Boolean {
        return dto1 == dto2
    }

    override fun entityToDto(e: GroupsEntity): GroupsDto {
        return GroupsDto(id = e.id, profile = e.profile, size = e.size, fileIds = e.fileIds)
    }

    override fun dtoToEntity(dto: GroupsDto): GroupsEntity {
        return with(dto) {
            GroupsEntity(id = dto.id ?: 0, profile = dto.profile, size = dto.size, fileIds = dto.fileIds ?: emptySet())
        }
    }

}