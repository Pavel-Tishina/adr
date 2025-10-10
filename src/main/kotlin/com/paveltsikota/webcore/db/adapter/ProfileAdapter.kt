package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.ProfileDto
import com.paveltsikota.webcore.db.entity.ProfileEntity
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE_DESCRIPTION
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE_TITLE
import org.springframework.stereotype.Component

@Component
object ProfileAdapter: AbstractEntityDtoAdapter<ProfileEntity, ProfileDto>(
    entityClass = ProfileEntity::class,
    dtoClass = ProfileDto::class
) {

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

    fun isDefaultProfile(e: ProfileEntity?): Boolean = e != null && e.id == 1L

    fun makeDefaultProfileEntity(): ProfileEntity {
        return ProfileEntity(
            id = DEFAULT_PROFILE,
            title = DEFAULT_PROFILE_TITLE,
            description = DEFAULT_PROFILE_DESCRIPTION,
            cfg = ConfigAdapter.getDefaultConfigMap()
        )
    }

}