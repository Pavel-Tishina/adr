package com.paveltsikota.webcore.utils.entity

import com.paveltsikota.webcore.db.entity.ProfileEntity
import com.paveltsikota.webcore.db.dto.ProfileDto

object ProfileEntityUtils {

    fun eq(e1: ProfileEntity, e2: ProfileEntity): Boolean {
        return e1.id == e2.id
                && e1.title == e2.title
                && e1.description == e2.description
                && e1.cfg == e2.cfg
    }

    fun entityToDto(e: ProfileEntity): ProfileDto {
        return ProfileDto(id = e.id, title = e.title, description = e.description, cfg = ConfigEntityUtils.mapToDto(e.cfg))
    }

    fun dtoToEntity(dto: ProfileDto): ProfileEntity {
        return with(dto) {
            ProfileEntity(id = id?:0, title = title, description = description?:"", cfg = ConfigEntityUtils.dtoToMap(cfg))
        }
    }


}