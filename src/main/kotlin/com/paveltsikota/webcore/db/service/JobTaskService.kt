package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.dto.HistoryElementDto
import com.paveltsikota.webcore.db.entity.JobsTaskEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.dto.JobsTaskDto
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import java.sql.Timestamp

interface JobTaskService {
    fun getById(id: Long): EntityOperationResult
    fun get(profile: Long?, priority: Int?, type: JobsType?, status: JobStatus?): EntityOperationResult

    fun getAllNotStarted(profile: Long?): List<JobsTaskEntity>
    fun getAllPaused(profile: Long?): List<JobsTaskEntity>
    fun getAllRun(profile: Long?): List<JobsTaskEntity>

    fun add(
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
    ): EntityOperationResult

    fun addDto(jobDto: Collection<JobsTaskDto>, addOnce: Boolean?): EntityOperationResult

    fun update(job: JobsTaskEntity): EntityOperationResult

    fun remove(id: Long): EntityOperationResult
    fun remove(job: JobsTaskEntity): EntityOperationResult

    fun cleanUp(profileId: Long): EntityOperationResult

    fun isAlreadyExist(job: JobsTaskEntity): Boolean
    fun canUpdate(job: JobsTaskEntity): Boolean
}