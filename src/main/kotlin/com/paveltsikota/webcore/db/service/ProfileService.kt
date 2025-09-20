package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.entity.ProfileEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult

interface ProfileService {
    fun getById(id: Long): EntityOperationResult
    fun getByTitle(title: String): EntityOperationResult

    fun add(title: String, description: String?, cfg: Map<String, Any>?, addOnce: Boolean?): EntityOperationResult

    fun update(profile: ProfileEntity): EntityOperationResult
    fun remove(id: Long): EntityOperationResult
    fun remove(profile: ProfileEntity): EntityOperationResult

    fun isAlreadyExist(sources: ProfileEntity): Boolean
}