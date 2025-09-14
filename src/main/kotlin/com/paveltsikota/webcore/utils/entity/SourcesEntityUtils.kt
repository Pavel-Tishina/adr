package com.paveltsikota.webcore.utils.entity

import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.rest.api.SourcesDto
import com.paveltsikota.webcore.utils.FileUtils
import kotlin.io.path.Path

object SourcesEntityUtils {

    fun eq(e1: SourcesEntity, e2: SourcesEntity): Boolean {
        return e1.id == e2.id
                && e1.profile == e2.profile
                && e1.dirorder == e2.dirorder
                && e1.path == e2.path
    }

    fun entityToDto(e: SourcesEntity): SourcesDto {
        return SourcesDto(id = e.id, profile = e.profile, dirorder = e.dirorder, path = e.path)
    }

    fun dtoToEntity(dto: SourcesDto): SourcesEntity {
        return with(dto) {
            SourcesEntity(id = id?: 0, profile = profile, dirorder = dirorder, path = FileUtils.toUnixPath(Path(path)))
        }
    }

}