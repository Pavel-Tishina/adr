package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.hash.calculator.HashCalculator
import com.paveltsikota.webcore.rest.api.FilesDto
import com.paveltsikota.webcore.utils.enums.HashType
import java.nio.file.Path

interface FilesService {
    fun getFile(id: Long): EntityOperationResult
    fun getFiles(ids: Collection<Long>): EntityOperationResult
    fun getFiles(page: Int, pageSize: Int, profileId: Long?): EntityOperationResult

    fun addLocalFile(path: Path, calc: HashCalculator?, addOnce: Boolean?): EntityOperationResult
    fun addRemoteFile(fileDto: FilesDto, addOnce: Boolean?): EntityOperationResult
    fun addRemoteFiles(fileDto: List<FilesDto>, addOnce: Boolean?): EntityOperationResult

    fun updateFile(file: FilesEntity): EntityOperationResult
    fun updateFile(fileFto: FilesDto, isLocal: Boolean?): EntityOperationResult

    fun removeFile(file: FilesEntity): EntityOperationResult
    fun removeFile(fileDto: FilesDto): EntityOperationResult
    fun removeFile(id: Long): EntityOperationResult
    fun removeFiles(ids: Collection<Long>): EntityOperationResult

    fun findByGroupId(groupId: Long, profileId: Long): EntityOperationResult
    fun findByHashId(hashId: Long, profileId: Long): EntityOperationResult
    fun findByHash(hash: String, hashType: HashType, profileId: Long): EntityOperationResult
    fun findBySize(page: Int?, pageSize: Int?, size: Long, profileId: Long): EntityOperationResult

    fun findNotGrouped(profileId: Long, size: Long?, page: Int, pageSize: Int): List<FilesEntity>?
    fun findAllNotGrouped(profileId: Long, size: Long?): List<FilesEntity>?

    fun findNotHashed(profileId: Long, size: Long?, page: Int, pageSize: Int): List<FilesEntity>?
    fun findAllNotHashed(profileId: Long, size: Long?): List<FilesEntity>?

    fun findNotCalculatedHash(profileId: Long, size: Long?, page: Int, pageSize: Int): List<FilesEntity>?
    fun findAllNotCalculatedHash(profileId: Long, size: Long?): List<FilesEntity>?

    fun getBySql(sql: String, params: Map<String, Any>, page: Int?, pageSize: Int?): List<FilesEntity>?

    fun cleanUp(profileId: Long): EntityOperationResult

    fun isAlreadyExist(file: FilesEntity): Boolean
}