package com.paveltsikota.webcore.db.mapper

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.rest.api.FilesDto
import com.paveltsikota.webcore.utils.entity.FilesEntityUtils

object EntityMapper {

    fun filesEntityToDtoList(e: Any?): List<FilesDto> = when (e) {
        is FilesEntity -> listOf(FilesEntityUtils.entityToDto(e))
        is List<*> -> e.filterIsInstance<FilesEntity>().map(FilesEntityUtils::entityToDto)
        else -> emptyList()
    }

}