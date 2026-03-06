package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.adapter.JobsTaskAdapter
import com.paveltsikota.webcore.db.dto.CleanUpDto
import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.dto.JobsTaskDto
import com.paveltsikota.webcore.db.service.JobTaskService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.rest.model.PostAddManyJobsRequest
import com.paveltsikota.webcore.rest.model.TypedResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getTypedResponse
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobStatus.CREATED
import com.paveltsikota.webcore.utils.enums.JobStatus.PAUSED
import com.paveltsikota.webcore.utils.enums.JobStatus.RUNNING
import com.paveltsikota.webcore.utils.enums.JobsType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/v1/jobs")
class JobsTaskController(private val jobTaskService: JobTaskService) {
    @GetMapping("/get/{id}")
    fun getJobById(@PathVariable id: Long): TypedResponse<List<JobsTaskDto>> {
        val opResult = jobTaskService.getById(id)

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @GetMapping("/get")
    fun getJobs(
        @RequestParam(required = false) profileId: Long?,
        @RequestParam(required = false) priority: Int?,
        @RequestParam(required = false) type: JobsType?,
        @RequestParam(required = false) status: JobStatus?
    ): TypedResponse<List<JobsTaskDto>> {
        val opResult = jobTaskService.get(profileId, priority, type, status)

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @GetMapping("/get-by-status")
    fun getJobByStatus(
        @RequestParam(required = false) profileId: Long,
        @RequestParam(required = false) status: JobStatus
    ): TypedResponse<List<JobsTaskDto>> {
        val opResult = when (status) {
            CREATED -> jobTaskService.getAllNotStarted(profileId)
            RUNNING -> jobTaskService.getAllRun(profileId)
            PAUSED -> jobTaskService.getAllPaused(profileId)
            else -> null
        }

        val finalResult = EntityOperationResult(success = true, obj = opResult, result = EntityOperationResultType.OK)
            .takeIf { opResult != null }
            ?: EntityOperationResult(success = false, error = "Wrong status set", result = EntityOperationResultType.ERROR)

        return getTypedResponse<List<JobsTaskDto>>(finalResult)
    }

    @PostMapping("/")
    fun addJob(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: JobsTaskDto
    ): TypedResponse<List<JobsTaskDto>> {
        val opResult = with(model) {
            jobTaskService.add(
                profile = profile,
                priority = priority,
                jobId = jobId,
                start = start,
                finish = finish,
                disabled = disabled,
                type = type,
                lastObjectId = lastObjectId,
                objects = objects,
                status = status,
                addOnce = addOnce,
                history = history,
            )
        }

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @PostMapping("/many")
    fun addManyJobs(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: PostAddManyJobsRequest
    ): TypedResponse<List<JobsTaskDto>> {
        val opResult = jobTaskService.addDto(model.jobs, addOnce)

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @PutMapping("/")
    fun updJob(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: JobsTaskDto
    ): TypedResponse<List<JobsTaskDto>> {
        val opResult = jobTaskService.update(JobsTaskAdapter.dtoToEntity(model))

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @DeleteMapping("/")
    fun delJob(@RequestBody model: GroupsDto): TypedResponse<List<JobsTaskDto>> {
        val opResult = jobTaskService.remove(model.id ?: 0)

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpJobs(@RequestParam(required = true) profileId: Long): TypedResponse<CleanUpDto> {
        val opResult = jobTaskService.cleanUp(profileId)

        return getTypedResponse<CleanUpDto>(opResult)
    }
}