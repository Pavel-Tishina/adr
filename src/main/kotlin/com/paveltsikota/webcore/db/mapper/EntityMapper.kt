package com.paveltsikota.webcore.db.mapper

import com.paveltsikota.webcore.db.dto.*
import com.paveltsikota.webcore.db.entity.*
import com.paveltsikota.webcore.rest.utils.ResponseMapperUtils
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

object EntityMapper {

    fun anyToDtoList(e: Any?): List<*> = when (e) {
        is Collection<*> -> e.mapNotNull(ResponseMapperUtils::anyToDto)
        else -> listOfNotNull(ResponseMapperUtils.anyToDto(e))
    }

    inline fun <reified T> filterAnyToDto(e: Any?): Any? = when (e) {
        is T -> ResponseMapperUtils.anyToDto(e)
        is Collection<*> -> e.filterIsInstance<T>().mapNotNull(ResponseMapperUtils::anyToDto)
        else -> null
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
        true -> filterAnyToDto<T>(e)
        else -> mapSingleToDto<E>(e)
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

    inline fun <reified T> isCollection(): Boolean = Collection::class.java.isAssignableFrom(T::class.java)

    inline fun <reified T> getCollectionIteratorClass(): KClass<*>? {
        val kType = typeOf<T>()
        val arg = kType.arguments.firstOrNull()?.type ?: return null
        return (arg.classifier as? KClass<*>)
    }

}