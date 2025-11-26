package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.ProfileDto
import com.paveltsikota.webcore.db.entity.ProfileEntity
import com.paveltsikota.webcore.db.utils.ConfigUtils.dtoToMap
import com.paveltsikota.webcore.db.utils.ConfigUtils.mapToDto
import org.springframework.stereotype.Component

@Component
object ProfileAdapter: AbstractEntityDtoAdapter<ProfileEntity, ProfileDto>(
    entityClass = ProfileEntity::class,
    dtoClass = ProfileDto::class
) {

    override fun entityToDto(e: ProfileEntity): ProfileDto {
        return ProfileDto(id = e.id, title = e.title, description = e.description, cfg = mapToDto(e.cfg))
    }

    override fun dtoToEntity(dto: ProfileDto): ProfileEntity {
        return with(dto) {
            ProfileEntity(id = id?:0, title = title, description = description?:"", cfg = dtoToMap(cfg))
        }
    }

}