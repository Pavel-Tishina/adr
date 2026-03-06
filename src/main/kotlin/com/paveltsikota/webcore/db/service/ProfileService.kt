package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.entity.ProfileEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.types.DataTypeAlias.*

interface ProfileService {
    fun getById(id: IdType): EntityOperationResult
    fun getByTitle(title: TitleType): EntityOperationResult

    fun add(title: TitleType, description: DescriptionType?, cfg: Map<String, Any>?, addOnce: AddOnceType?): EntityOperationResult

    fun update(profile: ProfileEntity): EntityOperationResult
    fun remove(id: IdType): EntityOperationResult
    fun remove(profile: ProfileEntity): EntityOperationResult

    fun isAlreadyExist(sources: ProfileEntity): Boolean
}