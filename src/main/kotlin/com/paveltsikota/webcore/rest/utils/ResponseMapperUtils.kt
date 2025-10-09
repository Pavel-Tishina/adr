package com.paveltsikota.webcore.rest.utils

import com.paveltsikota.webcore.db.dto.*
import com.paveltsikota.webcore.db.entity.*
import com.paveltsikota.webcore.utils.entity.*

object ResponseMapperUtils {

    fun anyToDto(e: Any?): Any? = when (e) {
        is Map<*, *> -> e.entries.associate { (k, v) -> anyToDto(k) to anyToDto(v) }
        is Collection<*> -> e.mapNotNull{ anyToDto(it) }
        else -> anyElementToDto(e)
    }

    fun anyElementToDto(e: Any?): Any? =
        primitiveMapper(e)
            ?: entityToDto(e)
            ?: dtoMapper(e)

    fun entityToDto(e: Any?): Any? = when (e) {
        is JobsEntity -> JobEntityUtils.entityToDto(e)
        is FilesEntity -> FilesEntityUtils.entityToDto(e)
        is GroupsEntity -> GroupsEntityUtils.entityToDto(e)
        is HashesEntity -> HashesEntityUtils.entityToDto(e)
        is SourcesEntity -> SourcesEntityUtils.entityToDto(e)
        is ProfileEntity -> ProfileEntityUtils.entityToDto(e)
        else -> null
    }

    fun dtoMapper(e: Any?): Any? = when (e) {
        is CfgDto, is DuplicateDto, is DuplicateFileDto, is FilesDto,
        is GroupsDto, is HashesDto, is JobsDto, is ProfileDto, is SourcesDto
            -> e
        else -> null
    }

    fun primitiveMapper(e: Any?): Any? = when (e) {
        is Number, is String, is Boolean, is Char, is Enum<*> -> e
        else -> null
    }
}