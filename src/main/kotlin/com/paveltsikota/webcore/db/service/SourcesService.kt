package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.dto.SourcesDto
import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import java.nio.file.Path

interface SourcesService {
    fun getSource(id: IdType): EntityOperationResult
    fun getSources(page: PageType?, pageSize: PageSizeType?, profileId: ProfileType?): EntityOperationResult
    fun getSources(ids: Collection<IdType>): EntityOperationResult

    fun addSource(path: Path, profileId: ProfileType, dirorder: DirOrderType, addOnce: AddOnceType?): EntityOperationResult
    fun addSourcesDto(sources: Collection<SourcesDto>, addOnce: AddOnceType?): EntityOperationResult

    fun updateSource(source: SourcesEntity): EntityOperationResult
    fun updateSources(sources: Collection<SourcesEntity>): EntityOperationResult
    fun updateSourcesDto(sources: Collection<SourcesDto>): EntityOperationResult

    fun removeSource(id: IdType): EntityOperationResult
    fun removeSource(source: SourcesEntity): EntityOperationResult
    fun removeSources(sources: Collection<SourcesEntity>): EntityOperationResult
    fun removeSourcesDto(sources: Collection<SourcesDto>): EntityOperationResult

    fun cleanUp(profileId: ProfileType): EntityOperationResult

    fun isAlreadyExist(sources: SourcesEntity, isCreate: Boolean): Boolean
}