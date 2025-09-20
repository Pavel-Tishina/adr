package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.dao.ProfileDao
import com.paveltsikota.webcore.db.entity.ProfileEntity
import com.paveltsikota.webcore.db.service.ProfileService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.utils.entity.ConfigEntityUtils.getDefaultConfigMap
import com.paveltsikota.webcore.utils.entity.ProfileEntityUtils.eq
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileDao: ProfileDao): ProfileService {
    override fun getById(id: Long): EntityOperationResult {
        return when (val entity = profileDao.findById(id)) {
            null -> EntityOperationResult(success = false, error = "Entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
            else -> EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    override fun getByTitle(title: String): EntityOperationResult {
        return when (val entity = profileDao.findByTitle(title)) {
            null -> EntityOperationResult(success = false, error = "Entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
            else -> EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    override fun add(title: String, description: String?, cfg: Map<String, Any>?, addOnce: Boolean?): EntityOperationResult {
        val newProfile = ProfileEntity(title = title, description = description?: "", cfg = cfg ?: getDefaultConfigMap())

        return if (addOnce == true && isAlreadyExist(newProfile)) {
            EntityOperationResult(success = false, error = "Entity already exist", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)
        } else {
            profileDao.save(newProfile)
            EntityOperationResult(success = true, obj = newProfile, result = EntityOperationResultType.ENTITY_ADD)
        }
    }

    override fun update(profile: ProfileEntity): EntityOperationResult {
        val obj = profileDao.update(profile)
        return if (eq(profile, obj)) {
            EntityOperationResult(success = true, obj = obj, result = EntityOperationResultType.ENTITY_UPDATED)
        } else {
            EntityOperationResult(success = false, obj = obj, result = EntityOperationResultType.ENTITY_NOT_UPDATED)
        }
    }

    override fun remove(id: Long): EntityOperationResult {
        return when (profileDao.removeById(id)) {
            false -> EntityOperationResult(success = false, error = "Entity not removed", result = EntityOperationResultType.ENTITY_NOT_REMOVED)
            true -> EntityOperationResult(success = true, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun remove(profile: ProfileEntity): EntityOperationResult {
        return remove(profile.id)
    }

    override fun isAlreadyExist(sources: ProfileEntity): Boolean {
        return profileDao.findByTitle(sources.title) != null
    }

}