package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.rest.api.FilesDto
import java.nio.file.Path

interface FilesService {
    fun getFile(id: Long): EntityOperationResult
    fun addFile(path: String): EntityOperationResult
    fun addFile(path: Path): EntityOperationResult
    fun addFileIfNotExist(path: Path): EntityOperationResult
    fun addFile(fileDto: FilesDto, calculateHash: Boolean): EntityOperationResult
    fun addFileIfNotExist(fileDto: FilesDto, calculateHash: Boolean): EntityOperationResult

    fun addLocalFile(path: Path): EntityOperationResult
    fun addRemoteFile(fileDto: FilesDto): EntityOperationResult

    fun updateFile(file: FilesEntity): EntityOperationResult
    fun updateFile(fileFto: FilesDto): EntityOperationResult
    fun removeFile(file: FilesEntity): EntityOperationResult
    fun removeFile(fileDto: FilesDto): EntityOperationResult
    fun removeFile(id: Long): EntityOperationResult
}