package com.paveltsikota.webcore.rest.utils

import com.paveltsikota.webcore.db.adapter.FilesAdapter
import com.paveltsikota.webcore.db.adapter.ConfigAdapter
import com.paveltsikota.webcore.db.adapter.GroupsAdapter
import com.paveltsikota.webcore.db.adapter.HashesAdapter
import com.paveltsikota.webcore.db.adapter.JobsAdapter
import com.paveltsikota.webcore.db.adapter.ProfileAdapter
import com.paveltsikota.webcore.db.adapter.SourcesAdapter
import com.paveltsikota.webcore.db.dto.CommonDto
import com.paveltsikota.webcore.db.entity.*

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
        is JobsEntity -> JobsAdapter.entityToDto(e)
        is FilesEntity -> FilesAdapter.entityToDto(e)
        is GroupsEntity -> GroupsAdapter.entityToDto(e)
        is HashesEntity -> HashesAdapter.entityToDto(e)
        is ConfigEntity -> ConfigAdapter.entityToDto(e)
        is SourcesEntity -> SourcesAdapter.entityToDto(e)
        is ProfileEntity -> ProfileAdapter.entityToDto(e)
        else -> null
    }

    fun dtoMapper(e: Any?): Any? = when (e) {
        is CommonDto -> e
        else -> null
    }

    fun primitiveMapper(e: Any?): Any? = when (e) {
        is Number, is String, is Boolean, is Char, is Enum<*> -> e
        else -> null
    }
}