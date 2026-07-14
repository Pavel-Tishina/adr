package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.dto.HistoryElementDto
import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.db.entity.JobsTaskEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.dto.JobsTaskDto
import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import java.sql.Timestamp

interface JobService {
    fun getById(id: Long): EntityOperationResult
    fun get(profile: Long?, uuid: String?, status: JobStatus?): EntityOperationResult

    fun getBy(profile: Long, global: Boolean?, disabled: Boolean?, status: JobStatus?): List<JobsEntity>

    fun add(
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
    ): EntityOperationResult

    fun addDto(jobDto: Collection<JobsDto>, addOnce: Boolean?): EntityOperationResult

    fun update(job: JobsEntity): EntityOperationResult

    fun remove(id: Long, profile: Long): EntityOperationResult
    fun remove(job: JobsEntity): EntityOperationResult

    fun cleanUp(profileId: Long): EntityOperationResult

    fun isAlreadyExist(job: JobsEntity): Boolean
    fun canUpdate(job: JobsEntity, existedJob: JobsEntity?): Boolean
}