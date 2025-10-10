package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.ProfileDto
import com.paveltsikota.webcore.db.entity.ProfileEntity
import org.springframework.stereotype.Component

@Component
object ProfileAdapter: AbstractEntityDtoAdapter<ProfileEntity, ProfileDto>(
    entityClass = ProfileEntity::class.java,
    dtoClass = ProfileDto::class.java
) {

    fun eq(e1: ProfileEntity, e2: ProfileEntity): Boolean {
        return e1.id == e2.id
                && e1.title == e2.title
                && e1.description == e2.description
                && e1.cfg == e2.cfg
    }

    override fun eqEntity(e1: ProfileEntity, e2: ProfileEntity): Boolean {
        return e1.id == e2.id
                && e1.title == e2.title
                && e1.description == e2.description
                && e1.cfg == e2.cfg
    }

    override fun eqDto(dto1: ProfileDto, dto2: ProfileDto): Boolean {
        return dto1 == dto2
    }

    override fun entityToDto(e: ProfileEntity): ProfileDto {
        return ProfileDto(id = e.id, title = e.title, description = e.description, cfg = ConfigAdapter.mapToDto(e.cfg))
    }

    override fun dtoToEntity(dto: ProfileDto): ProfileEntity {
        return with(dto) {
            ProfileEntity(id = id?:0, title = title, description = description?:"", cfg = ConfigAdapter.dtoToMap(cfg))
        }
    }

}