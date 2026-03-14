package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.constants.DbConst
import com.paveltsikota.webcore.db.dao.JobsTaskDao
import com.paveltsikota.webcore.db.dto.HistoryElementDto
import com.paveltsikota.webcore.db.dto.JobsTaskDto
import com.paveltsikota.webcore.db.entity.JobsTaskEntity
import com.paveltsikota.webcore.db.service.JobTaskService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.utils.ValuesUtils.priorityChk
import com.paveltsikota.webcore.utils.ValuesUtils.profileIdChk
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class JobTaskServiceImpl(
    private val jobsTaskDao: JobsTaskDao
): JobTaskService {
    override fun getById(id: Long): EntityOperationResult {
        return when (val entity = jobsTaskDao.findById(id)) {
            null -> EntityOperationResult(success = false, error = "Entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
            else -> EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    override fun get(profile: Long?, priority: Int?, type: JobsType?, status: JobStatus?): EntityOperationResult {
        val params = getMapForGet(profile, priority, type, status)
        val and = "AND".takeIf { params.size > 1 }?: ""

        val sql = ("FROM JobsEntity p " +
                (if (params.isNotEmpty()) {"WHERE "} else {""}) +
                (if (profileIdChk(profile)) {"$and p.profile = :profile "} else {""}) +
                (if (priorityChk(priority)) {"$and p.priority = :priority "} else {""}) +
                (if (type != null) {"$and p.type = :type "} else {""}) +
                (if (status != null) {"$and p.status = :status "} else {""}) +
                "ORDER BY p.priority")
            .replaceFirst("WHERE AND", "WHERE")

        val result = jobsTaskDao.getBySql(sql, params)

        return when {
            result.isNullOrEmpty() -> EntityOperationResult(
                success = false, error = "Entities not founded", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            result.size == 1 -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITY_FOUND)

            else -> EntityOperationResult(
                success = true, obj = result, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun getAllNotStarted(profile: Long?): List<JobsTaskEntity> {
        return getAllByStatus(profile, JobStatus.CREATED)
    }

    override fun getAllPaused(profile: Long?): List<JobsTaskEntity> {
        return getAllByStatus(profile, JobStatus.PAUSED)
    }

    override fun getAllRun(profile: Long?): List<JobsTaskEntity> {
        return getAllByStatus(profile, JobStatus.RUNNING)
    }

    override fun add(
        profile: Long,
        priority: Int?,
        jobId: Long?,
        start: Timestamp?,
        finish: Timestamp?,
        disabled: Boolean?,
        type: JobsType,
        lastObjectId: Long?,
        status: JobStatus,
        objects: MutableList<Long>?,
        history: MutableList<HistoryElementDto>?,
        addOnce: Boolean?
    ): EntityOperationResult {
        val newJob = JobsTaskEntity(
            profile = profile,
            priority = priority.takeIf { priority != null }?: jobsTaskDao.findLastPriority(profile),
            start = start?.time,
            finish = finish?.time,
            disabled = disabled == true,
            type = type,
            lastObjectId = lastObjectId,
            status = status,
            objects = objects,
//            history = history?.map(historyElementAdapter::dtoToEntity) as MutableList<HistoryElementEntity>?,
            history = history,
        )

        return if (!profileIdChk(profile)) {
            EntityOperationResult(success = false, error = "Wrong profile value", result = EntityOperationResultType.ENTITY_NOT_ADD)
        } else if (addOnce == true && isAlreadyExist(newJob)) {
            EntityOperationResult(success = false, error = "Entity already exist", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)
        } else {
            jobsTaskDao.save(newJob)
            EntityOperationResult(success = true, obj = newJob, result = EntityOperationResultType.ENTITY_ADD)
        }
    }

    override fun addDto(jobDto: Collection<JobsTaskDto>, addOnce: Boolean?): EntityOperationResult {
        val filteredJobDtos = jobDto.distinct()
        val errors = mutableSetOf<JobsTaskDto>()
        // val errorsProfile = mutableSetOf<JobsDto>() //  TODO
        val added = mutableSetOf<JobsTaskEntity>()

        filteredJobDtos.forEach { dto ->
            val result = with(dto) {
                add(profile, priority, jobId, start, finish, disabled, type, lastObjectId, status, objects, history, addOnce)
//                add(profile, priority, jobId, start, finish, disabled, type, lastObjectId, status, objects, addOnce)
            }
            if (result.success) {
                added.add(result.obj as JobsTaskEntity)
            } else {
                errors.add(dto)
            }
        }

        return when {
            errors.size == filteredJobDtos.size -> EntityOperationResult(
                success = false, error = "All entities are already exist or has wrong profile", result = EntityOperationResultType.ENTITIES_ALREADY_EXISTED)

            errors.isNotEmpty() -> EntityOperationResult(
                success = true, error = "Not added entities: ${errors.joinToString("\n")}", obj = added, result = EntityOperationResultType.ENTITIES_ADDED_PARTLY)

            else -> EntityOperationResult(
                success = true, obj = added, result = EntityOperationResultType.ENTITIES_ADDED)
        }
    }

    override fun update(job: JobsTaskEntity): EntityOperationResult {
        val obj = if (canUpdate(job)) { jobsTaskDao.update(job) } else { null }
        return when {
            obj == null -> EntityOperationResult(
                success = false, error = "Jobs entity not found", obj = job, result = EntityOperationResultType.ENTITY_NOT_FOUND)

            job != obj -> EntityOperationResult(
                success = false, error = "Entity not updated", obj = job, result = EntityOperationResultType.ENTITY_NOT_UPDATED)

            else -> EntityOperationResult(
                success = true, obj = obj, result = EntityOperationResultType.ENTITY_UPDATED)
        }
    }

    // TODO remove by id and profileID
    override fun remove(id: Long): EntityOperationResult {
        return when (jobsTaskDao.removeById(id)) {
            false -> EntityOperationResult(success = false, error = "Entity not removed", result = EntityOperationResultType.ENTITY_NOT_REMOVED)
            true -> EntityOperationResult(success = true, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun remove(job: JobsTaskEntity): EntityOperationResult {
        return when (jobsTaskDao.removeByIdAndProfile(job.id, job.profile)) {
            false -> EntityOperationResult(success = false, error = "Entity not removed", result = EntityOperationResultType.ENTITY_NOT_REMOVED)
            true -> EntityOperationResult(success = true, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun cleanUp(profileId: Long): EntityOperationResult {
        var count = 0
        var page = 0
        val notDeleted = HashSet<Long>()
        do {
            page = page + 1
            val partResult = jobsTaskDao.getAll(page = page, pageSize = DbConst.MAX_PAGE_SIZE, profileId = profileId)?:emptyList()

            if (partResult.isNotEmpty()) {
                partResult.forEach { if (!jobsTaskDao.removeById((it).id)) notDeleted.add(it.id) }
                count += partResult.size
            }
        } while (partResult.isNotEmpty())

        return when {
            count == 0 -> EntityOperationResult(
                success = false, error = "There is no entities by profile '$profileId'",
                result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)

            notDeleted.isNotEmpty() -> EntityOperationResult(
                success = true, error = "Next entities not deleted ${notDeleted.joinToString(separator = ", ")}", obj = count,
                result = EntityOperationResultType.ENTITIES_REMOVED_PARTLY)

            else -> EntityOperationResult(
                success = true, obj = count,
                result = EntityOperationResultType.ENTITIES_REMOVED)
        }
    }

    override fun canUpdate(job: JobsTaskEntity): Boolean {
        return with (getById(job.id)) {
            success && (obj as JobsTaskEntity).profile == job.profile && obj.type == job.type
                    && profileIdChk(job.profile) && priorityChk(job.priority)
        }
    }

    override fun isAlreadyExist(job: JobsTaskEntity): Boolean {
        return get(job.profile, job.priority, job.type, job.status).success
    }

    private fun getMapForGet(profile: Long?, priority: Int?, type: JobsType?, status: JobStatus?): Map<String, Any> {
        val map = mutableMapOf<String, Any>()

        if (priorityChk(priority)) map["priority"] = priority!!
        if (profileIdChk(profile)) map["profile"] = profile!!
        if (status != null) map["status"] = status
        if (type != null) map["type"] = type

        return map
    }

    @Suppress("UNCHECKED_CAST")
    private fun getAllByStatus(profileId: Long?, status: JobStatus): List<JobsTaskEntity> {
        val jobs = get(profile = profileId, status = status, priority = null, type = null)

        return when {
            jobs.success -> jobs.obj as List<JobsTaskEntity>
            else -> emptyList()
        }
    }

}