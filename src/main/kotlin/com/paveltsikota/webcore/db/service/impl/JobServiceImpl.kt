package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.constants.DbConst
import com.paveltsikota.webcore.db.dao.JobsDao
import com.paveltsikota.webcore.db.dto.HistoryElementDto
import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.db.service.JobService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.db.type.QueryParamMap
import com.paveltsikota.webcore.db.type.toSqlQuery
import com.paveltsikota.webcore.utils.ValuesUtils.isFinalJobState
import com.paveltsikota.webcore.utils.ValuesUtils.priorityChk
import com.paveltsikota.webcore.utils.ValuesUtils.profileIdChk
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class JobServiceImpl(
    private val jobsDao: JobsDao
): JobService {
    override fun getById(id: Long): EntityOperationResult {
        return when (val entity = jobsDao.findById(id)) {
            null -> EntityOperationResult(success = false, error = "Entity not found", result = EntityOperationResultType.ENTITY_NOT_FOUND)
            else -> EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        }
    }

    override fun get(profile: Long?, uuid: String?, status: JobStatus?): EntityOperationResult {
        val result = if (profile != null) {
            if (uuid != null) {
                jobsDao.findByUuidAndProfileId(uuid, profile)
            } else if (status != null) {
                jobsDao.findByStatusAndProfileId(status, profile)
            } else {
                jobsDao.findByProfileId(profile)
            }
        } else {
            null
        }

        return when {
            result == null ->
                EntityOperationResult(success = false, error = "Bad params", result = EntityOperationResultType.ERROR)
            result.isEmpty() ->
                EntityOperationResult(success = false, error = "Entities not founded", result = EntityOperationResultType.ENTITIES_NOT_FOUNDED)
            else ->
                EntityOperationResult(success = true, obj = result, result = EntityOperationResultType.ENTITIES_FOUNDED)
        }
    }

    override fun getBy(profile: Long, global: Boolean?, disabled: Boolean?, status: JobStatus?): List<JobsEntity> {
        val params: QueryParamMap = linkedMapOf("profile" to profile)
        params.putAll(getMapForGet(global, disabled, status))

        val sql = "FROM JobsEntity p ${params.toSqlQuery("p")} ORDER BY p.start"
        return jobsDao.getBySql(sql, params)?:emptyList()
    }

    override fun add(
        profile: Long,
        uuid: String?,
        global: Boolean?,
        start: Timestamp?,
        finish: Timestamp?,
        disabled: Boolean?,
        taskList: MutableList<Long>,
        history: MutableList<HistoryElementDto>?,
        status: JobStatus,
        addOnce: Boolean?
    ): EntityOperationResult {
        val e = JobsEntity(
            profile = profile, uuid = uuid, global = global?: false, start = start?.time, finish = finish?.time,
            disabled = disabled?: false, taskList = taskList, history = history?: mutableListOf(), status = status
        )
        return if (addOnce == true && isAlreadyExist(e)) {
            EntityOperationResult(success = false, error = "Already exist", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)
        } else {
            jobsDao.save(e)
            EntityOperationResult(success = true, obj = e, result = EntityOperationResultType.ENTITY_ADD)
        }
    }

    override fun addDto(jobDto: Collection<JobsDto>, addOnce: Boolean?): EntityOperationResult {
        val errors = mutableSetOf<JobsDto>()
        val added = mutableSetOf<JobsEntity>()

        jobDto.distinct().forEach { dto -> with(dto) {
            add(profile, uuid, global, start, finish, disabled, taskList, history, status, addOnce).let {
                if (it.success) {
                    added.add(it.obj as JobsEntity)
                } else {
                    errors.add(dto)
                }
            }
        }}

        return when {
            added.isEmpty() -> EntityOperationResult(
                success = false, error = "All entities are already exist", result = EntityOperationResultType.ENTITIES_ALREADY_EXISTED)

            errors.isNotEmpty() -> EntityOperationResult(
                success = true, error = "Not added entities: ${errors.joinToString("\n")}", obj = added, result = EntityOperationResultType.ENTITIES_ADDED_PARTLY)

            else -> EntityOperationResult(
                success = true, obj = added, result = EntityOperationResultType.ENTITIES_ADDED)
        }
    }

    override fun update(job: JobsEntity): EntityOperationResult {
        val obj = if (canUpdate(job, jobsDao.findById(job.id))) { jobsDao.update(job) } else { null }
        return when {
            obj == null -> EntityOperationResult(
                success = false, error = "Jobs entity not found", obj = job, result = EntityOperationResultType.ENTITY_NOT_FOUND)

            job != obj -> EntityOperationResult(
                success = false, error = "Entity not updated", obj = job, result = EntityOperationResultType.ENTITY_NOT_UPDATED)

            else -> EntityOperationResult(
                success = true, obj = obj, result = EntityOperationResultType.ENTITY_UPDATED)
        }
    }

    override fun remove(id: Long, profile: Long): EntityOperationResult {
        return when (jobsDao.removeByIdAndProfile(id, profile)) {
            false -> EntityOperationResult(success = false, error = "Entity not removed", result = EntityOperationResultType.ENTITY_NOT_REMOVED)
            true -> EntityOperationResult(success = true, result = EntityOperationResultType.ENTITY_REMOVED)
        }
    }

    override fun remove(job: JobsEntity): EntityOperationResult {
        return remove(job.id, job.profile)
    }

    override fun cleanUp(profileId: Long): EntityOperationResult {
        var count = 0
        var page = 0
        val notDeleted = HashSet<Long>()
        do {
            page += 1
            val partResult = jobsDao.getAll(page = page, pageSize = DbConst.MAX_PAGE_SIZE, profileId = profileId)?:emptyList()

            if (partResult.isNotEmpty()) {
                partResult.forEach { if (!jobsDao.removeById((it).id)) notDeleted.add(it.id) }
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

    override fun isAlreadyExist(job: JobsEntity): Boolean = jobsDao.findById(job.id).let { it?.equals(job) ?: false }

    override fun canUpdate(job: JobsEntity, existedJob: JobsEntity?): Boolean {
        return existedJob?.let {
            it.id == job.id && it.finish == job.finish && it.profile == job.profile && !isFinalJobState(it.status)
        } ?: true
    }

    private fun getMapForGet(global: Boolean?, disabled: Boolean?, status: JobStatus?): Map<String, Any> {
        val map = mutableMapOf<String, Any>()

        disabled?.let { map["disabled"] = disabled }
        global?.let { map["global"] = global }
        status?.let { map["status"] = status }

        return map
    }

}