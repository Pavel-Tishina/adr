package com.paveltsikota.webcore.db.mapper

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.entity.GroupsEntity
import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.db.entity.ProfileEntity
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.db.dto.FilesDto
import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.dto.HashesDto
import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.db.dto.ProfileDto
import com.paveltsikota.webcore.db.dto.SourcesDto
import com.paveltsikota.webcore.utils.entity.FilesEntityUtils
import com.paveltsikota.webcore.utils.entity.GroupsEntityUtils
import com.paveltsikota.webcore.utils.entity.HashesEntityUtils
import com.paveltsikota.webcore.utils.entity.JobEntityUtils
import com.paveltsikota.webcore.utils.entity.ProfileEntityUtils
import com.paveltsikota.webcore.utils.entity.SourcesEntityUtils

object EntityMapper {

    fun filesEntityToDtoList(e: Any?): List<FilesDto> = when (e) {
        is FilesEntity -> listOf(FilesEntityUtils.entityToDto(e))
        is Collection<*> -> e.filterIsInstance<FilesEntity>().map(FilesEntityUtils::entityToDto)
        else -> emptyList()
    }

    fun groupsEntityToDtoList(e: Any?): List<GroupsDto> = when (e) {
        is GroupsEntity -> listOf(GroupsEntityUtils.entityToDto(e))
        is Collection<*> -> e.filterIsInstance<GroupsEntity>().map(GroupsEntityUtils::entityToDto)
        else -> emptyList()
    }

    fun hashesEntityToDtoList(e: Any?): List<HashesDto> = when (e) {
        is HashesEntity -> listOf(HashesEntityUtils.entityToDto(e))
        is Collection<*> -> e.filterIsInstance<HashesEntity>().map(HashesEntityUtils::entityToDto)
        else -> emptyList()
    }

    fun jobsEntityToDtoList(e: Any?): List<JobsDto> = when (e) {
        is JobsEntity -> listOf(JobEntityUtils.entityToDto(e))
        is Collection<*> -> e.filterIsInstance<JobsEntity>().map(JobEntityUtils::entityToDto)
        else -> emptyList()
    }

    fun profileEntityToDtoList(e: Any?): List<ProfileDto> = when (e) {
        is ProfileEntity -> listOf(ProfileEntityUtils.entityToDto(e))
        is Collection<*> -> e.filterIsInstance<ProfileEntity>().map(ProfileEntityUtils::entityToDto)
        else -> emptyList()
    }

    fun sourceEntityToDtoList(e: Any?): List<SourcesDto> = when (e) {
        is SourcesEntity -> listOf(SourcesEntityUtils.entityToDto(e))
        is Collection<*> -> e.filterIsInstance<SourcesEntity>().map(SourcesEntityUtils::entityToDto)
        else -> emptyList()
    }

}