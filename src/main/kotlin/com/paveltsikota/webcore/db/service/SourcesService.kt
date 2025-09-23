package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.dto.SourcesDto
import java.nio.file.Path

interface SourcesService {
    fun getSource(id: Long): EntityOperationResult
    fun getSources(page: Int?, pageSize: Int?, profileId: Long?): EntityOperationResult

    fun addSource(path: Path, profileId: Long, dirorder: Int, addOnce: Boolean?): EntityOperationResult
    fun addSourcesDto(sources: Collection<SourcesDto>, addOnce: Boolean?): EntityOperationResult

    fun updateSource(source: SourcesEntity): EntityOperationResult
    fun updateSources(sources: Collection<SourcesEntity>): EntityOperationResult
    fun updateSourcesDto(sources: Collection<SourcesDto>): EntityOperationResult

    fun removeSource(id: Long): EntityOperationResult
    fun removeSource(source: SourcesEntity): EntityOperationResult
    fun removeSources(sources: Collection<SourcesEntity>): EntityOperationResult
    fun removeSourcesDto(sources: Collection<SourcesDto>): EntityOperationResult

    fun cleanUp(profileId: Long): EntityOperationResult

    fun isAlreadyExist(sources: SourcesEntity): Boolean
}