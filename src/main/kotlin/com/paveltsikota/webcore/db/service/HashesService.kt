package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.utils.enums.HashType

interface HashesService {
    fun getById(id: Long): EntityOperationResult
    fun getByIds(ids: Collection<Long>): EntityOperationResult
    fun getAll(page: Int?, pageSize: Int?, profileId: Long?): EntityOperationResult
    fun getAllByN(page: Int?, pageSize: Int?, profileId: Long?, n: Int?): EntityOperationResult

    fun add(profileId: Long, size: Long, hash: String, hashType: HashType, main: Long, dupIds: Collection<Long>?, addOnce: Boolean?): EntityOperationResult

    fun update(entity: HashesEntity): EntityOperationResult
    fun update(id: Long, profileId: Long, main: Long, dupIds: Collection<Long>?): EntityOperationResult

    fun remove(id: Long): EntityOperationResult
    fun remove(entity: HashesEntity): EntityOperationResult

    fun findByHash(hash: String, hashType: HashType, profileId: Long): EntityOperationResult
    fun findBySize(page: Int?, pageSize: Int?, size: Long, profileId: Long): EntityOperationResult

    fun getBySql(sql: String, params: Map<String, Any>, page: Int?, pageSize: Int?): List<HashesEntity>?

    fun cleanUp(profileId: Long): EntityOperationResult

    fun isAlreadyExist(e: HashesEntity): Boolean
}