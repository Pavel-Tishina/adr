package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import java.sql.Timestamp

interface JobService {
    fun getById(id: Long): EntityOperationResult
    fun get(profile: Long?, priority: Int?, type: JobsType?, status: JobStatus?): EntityOperationResult

    fun getAllNotStarted(profile: Long?): List<JobsEntity>
    fun getAllPaused(profile: Long?): List<JobsEntity>
    fun getAllRun(profile: Long?): List<JobsEntity>

    fun add(
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
    ): EntityOperationResult

    fun addDto(jobDto: Collection<JobsDto>, addOnce: Boolean?): EntityOperationResult

    fun update(job: JobsEntity): EntityOperationResult

    fun remove(id: Long): EntityOperationResult
    fun remove(job: JobsEntity): EntityOperationResult

    fun cleanUp(profileId: Long): EntityOperationResult

    fun isAlreadyExist(job: JobsEntity): Boolean
    fun canUpdate(job: JobsEntity): Boolean
}