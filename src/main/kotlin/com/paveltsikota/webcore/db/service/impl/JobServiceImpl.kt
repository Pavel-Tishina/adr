package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.dao.JobsDao
import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.db.service.JobService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.utils.entity.JobEntityUtils.eq
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class JobServiceImpl(private val jobsDao: JobsDao): JobService {
    override fun getById(id: Long): EntityOperationResult {
        return when (val entity = jobsDao.findById(id)) {
            null -> EntityOperationResult(success = false, error = "Entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
            else -> EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    override fun get(profile: Long?, priority: Int?, type: JobsType?, status: JobStatus?): EntityOperationResult {
        val params = getMapForGet(profile, priority, type, status)
        val and = "AND".takeIf { params.size > 1 }?: ""

        val sql = "FROM JobsEntity p " +
                "${if (params.isNotEmpty()) {"WHERE "} else {}}" +
                "${if (profile != null) {"$and p.profile = :profile "} else {}}" +
                "${if (priority != null) {"$and p.priority = :priority "} else {}}" +
                "${if (type != null) {"$and p.type = :type "} else {}}" +
                "${if (status != null) {"$and p.status = :status "} else {}}" +
                "ORDER BY p.priority"

        val result = jobsDao.getBySql(sql, params)

        return when {
            result.isNullOrEmpty() -> EntityOperationResult(
                success = false, error = "Entities not founded", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            result.size == 1 -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITY_FOUND)

            else -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun getAllNotStarted(profile: Long?): List<JobsEntity> {
        val jobs = get(profile = profile, status = JobStatus.CREATED, priority = null, type = null)

        return when {
            jobs.success -> jobs.obj as List<JobsEntity>
            else -> emptyList()
        }
    }

    override fun getAllPaused(profile: Long?): List<JobsEntity> {
        val jobs = get(profile = profile, status = JobStatus.PAUSED, priority = null, type = null)

        return when {
            jobs.success -> jobs.obj as List<JobsEntity>
            else -> emptyList()
        }
    }

    override fun getAllRun(profile: Long?): List<JobsEntity> {
        val jobs = get(profile = profile, status = JobStatus.RUNNING, priority = null, type = null)

        return when {
            jobs.success -> jobs.obj as List<JobsEntity>
            else -> emptyList()
        }
    }

    override fun add(
        profile: Long,
        priority: Int?,
        start: Timestamp?,
        finish: Timestamp?,
        completed: Boolean?,
        disabled: Boolean?,
        type: JobsType,
        lastObject: String?,
        lastObjectId: Long?,
        status: JobStatus,
        addOnce: Boolean?
    ): EntityOperationResult {
        val newJob = JobsEntity(
            profile = profile,
            priority = priority.takeIf { priority != null }?: jobsDao.findLastPriority(profile),
            start = start,
            finish = finish,
            completed = completed,
            disabled = disabled == true,
            type = type,
            lastObject = lastObject,
            lastObjectId = lastObjectId,
            status = status,
        )

        return if (addOnce == true && isAlreadyExist(newJob)) {
            EntityOperationResult(success = false, error = "Entity already exist", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)
        } else {
            jobsDao.save(newJob)
            EntityOperationResult(success = true, obj = newJob, result = EntityOperationResultType.ENTITY_ADD)
        }
    }

    override fun addDto(jobDto: Collection<JobsDto>, addOnce: Boolean?): EntityOperationResult {
        val filteredJobDtos = jobDto.distinct()
        val errors = mutableSetOf<JobsDto>()
        val added = mutableSetOf<JobsEntity>()

        filteredJobDtos.forEach { dto ->
            val result = with(dto) {
                add(profile, priority, start, finish, completed, disabled, type, lastObject, lastObjectId, status, addOnce)
            }
            if (result.success) {
                added.add(result.obj as JobsEntity)
            } else {
                errors.add(dto)
            }
        }

        return when {
            errors.size == filteredJobDtos.size -> EntityOperationResult(
                success = false, error = "All entities are already exist", result = EntityOperationResultType.ENTITIES_ALREADY_EXISTED)

            errors.isNotEmpty() -> EntityOperationResult(
                success = true, error = "Not added entities: ${errors.joinToString("\n")}", obj = added, result = EntityOperationResultType.ENTITIES_ADDED_PARTLY)

            else -> EntityOperationResult(
                success = true, obj = added, result = EntityOperationResultType.ENTITIES_ADDED)
        }
    }

    override fun update(job: JobsEntity): EntityOperationResult {
        val obj = jobsDao.update(job)
        return if (eq(job, obj)) {
            EntityOperationResult(success = true, obj = obj, result = EntityOperationResultType.ENTITY_UPDATED)
        } else {
            EntityOperationResult(success = false, obj = obj, result = EntityOperationResultType.ENTITY_NOT_UPDATED)
        }
    }

    override fun remove(id: Long): EntityOperationResult {
        return when (jobsDao.removeById(id)) {
            false -> EntityOperationResult(success = false, error = "Entity not removed", result = EntityOperationResultType.ENTITY_NOT_REMOVED)
            true -> EntityOperationResult(success = true, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun remove(job: JobsEntity): EntityOperationResult {
        return remove(job.id)
    }

    override fun isAlreadyExist(job: JobsEntity): Boolean {
        return get(job.profile, job.priority, job.type, job.status).success
    }

    private fun getMapForGet(profile: Long?, priority: Int?, type: JobsType?, status: JobStatus?): Map<String, Any> {
        val map = mutableMapOf<String, Any>()

        if (priority != null && priority >= 0) map["priority"] = priority
        if (profile != null && profile >= 0) map["profile"] = profile
        if (status != null) map["status"] = status
        if (type != null) map["type"] = type

        return map
    }

}