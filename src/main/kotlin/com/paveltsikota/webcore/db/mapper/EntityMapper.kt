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
import com.paveltsikota.webcore.db.mapper.EntityMapper.isCollection
import com.paveltsikota.webcore.rest.utils.ResponseMapperUtils
import com.paveltsikota.webcore.utils.entity.FilesEntityUtils
import com.paveltsikota.webcore.utils.entity.GroupsEntityUtils
import com.paveltsikota.webcore.utils.entity.HashesEntityUtils
import com.paveltsikota.webcore.utils.entity.JobEntityUtils
import com.paveltsikota.webcore.utils.entity.ProfileEntityUtils
import com.paveltsikota.webcore.utils.entity.SourcesEntityUtils
import org.springframework.data.util.CustomCollections.isCollection
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

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

    fun anyToDtoList(e: Any?): List<*> = when (e) {
        is Collection<*> -> e.mapNotNull(ResponseMapperUtils::anyToDto)
        else -> listOfNotNull(ResponseMapperUtils.anyToDto(e))
    }


    inline fun <reified T> filterAnyToDto(e: Any?): Any? = when (e) {
        is T -> ResponseMapperUtils.anyToDto(e)
        is Collection<*> -> e.filterIsInstance<T>().map {ResponseMapperUtils.anyToDto(e) }
        else -> emptyList<T>()
    }

    inline fun <reified T> mapSingleToDto(e: Any?): T? = when (T::class) {
        JobsDto::class -> filterAnyToDto<JobsEntity>(e)
        FilesDto::class -> filterAnyToDto<FilesEntity>(e)
        GroupsDto::class -> filterAnyToDto<GroupsEntity>(e)
        HashesDto::class -> filterAnyToDto<HashesEntity>(e)
        ProfileDto::class -> filterAnyToDto<ProfileEntity>(e)
        SourcesDto::class -> filterAnyToDto<SourcesEntity>(e)
        else -> null
    } as T?

    // for get responses like TypedResponse<List<JobsDto>>
    // call it like anyToTypedDtoInternal<List<JobsDto>, JobsDto>(e)
    // !!! but better use anyToTypedDto<List<JobsDto>>(e)  !!!
    inline fun <reified T, reified E> anyToTypedDtoInternal(e: Any?, isCollection: Boolean = false): Any? = when (isCollection) {
        true -> listOf(mapSingleToDto<E>(e))
        else -> mapSingleToDto<T>(e)
    }

    // for get responses like TypedResponse<List<JobsDto>>
    inline fun <reified T> anyToTypedDto(e: Any?): Any? {
        val innerClass  = getCollectionIteratorClass<T>()

        println("T::class = ${T::class}, innerClass = $innerClass")

        return if (isCollection<T>()) {
           when (innerClass) {
               JobsDto::class -> anyToTypedDtoInternal<T, JobsDto>(e, true)
               FilesDto::class -> anyToTypedDtoInternal<T, FilesDto>(e, true)
               GroupsDto::class -> anyToTypedDtoInternal<T, GroupsDto>(e, true)
               HashesDto::class -> anyToTypedDtoInternal<T, HashesDto>(e, true)
               ProfileDto::class -> anyToTypedDtoInternal<T, ProfileDto>(e, true)
               SourcesDto::class -> anyToTypedDtoInternal<T, SourcesDto>(e, true)
               else -> null
           }
        } else {
            anyToTypedDtoInternal<T, T>(e)
        }
    }

//    fun getCollectionIteratorClass(clazz: Class<*>): Any? {
//        return clazz::class.supertypes
//            .firstOrNull()
//            ?.arguments
//            ?.firstOrNull()
//            ?.type
//            ?.classifier as KClass<*>
//    }

    inline fun <reified T> isCollection(): Boolean = Collection::class.java.isAssignableFrom(T::class.java)

    inline fun <reified T> getCollectionIteratorClass(): KClass<*>? {
        val kType = typeOf<T>()
        val arg = kType.arguments.firstOrNull()?.type ?: return null
        return (arg.classifier as? KClass<*>)
    }

}