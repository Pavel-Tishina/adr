package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.adapter.JobsAdapter
import com.paveltsikota.webcore.db.dto.CleanUpDto
import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.db.service.JobService
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
class JobsController(private val jobService: JobService) {
    @GetMapping("/get/{id}")
    fun getJobById(@PathVariable id: Long): TypedResponse<List<JobsDto>> {
        val opResult = jobService.getById(id)

        return getTypedResponse<List<JobsDto>>(opResult)
    }

    @GetMapping("/get")
    fun getJobs(
        @RequestParam(required = false) profileId: Long?,
        @RequestParam(required = false) priority: Int?,
        @RequestParam(required = false) type: JobsType?,
        @RequestParam(required = false) status: JobStatus?
    ): TypedResponse<List<JobsDto>> {
        val opResult = jobService.get(profileId, priority, type, status)

        return getTypedResponse<List<JobsDto>>(opResult)
    }

    @GetMapping("/get-by-status")
    fun getJobByStatus(
        @RequestParam(required = false) profileId: Long,
        @RequestParam(required = false) status: JobStatus
    ): TypedResponse<List<JobsDto>> {
        val opResult = when (status) {
            CREATED -> jobService.getAllNotStarted(profileId)
            RUNNING -> jobService.getAllRun(profileId)
            PAUSED -> jobService.getAllPaused(profileId)
            else -> null
        }

        val finalResult = EntityOperationResult(success = true, obj = opResult, result = EntityOperationResultType.OK)
            .takeIf { opResult != null }
            ?: EntityOperationResult(success = false, error = "Wrong status set", result = EntityOperationResultType.ERROR)

        return getTypedResponse<List<JobsDto>>(finalResult)
    }

    @PostMapping("/")
    fun addJob(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: JobsDto
    ): TypedResponse<List<JobsDto>> {
        val opResult = with(model) {
            jobService.add(
                profile = profile,
                priority = priority,
                start = start,
                finish = finish,
                disabled = disabled,
                type = type,
                lastObject = lastObject,
                lastObjectId = lastObjectId,
                objects = objects,
                status = status,
                addOnce = addOnce,
                history = history,
            )
        }

        return getTypedResponse<List<JobsDto>>(opResult)
    }

    @PostMapping("/many")
    fun addManyJobs(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: PostAddManyJobsRequest
    ): TypedResponse<List<JobsDto>> {
        val opResult = jobService.addDto(model.jobs, addOnce)

        return getTypedResponse<List<JobsDto>>(opResult)
    }

    @PutMapping("/")
    fun updJob(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: JobsDto
    ): TypedResponse<List<JobsDto>> {
        val opResult = jobService.update(JobsAdapter.dtoToEntity(model))

        return getTypedResponse<List<JobsDto>>(opResult)
    }

    @DeleteMapping("/")
    fun delJob(@RequestBody model: GroupsDto): TypedResponse<List<JobsDto>> {
        val opResult = jobService.remove(model.id ?: 0)

        return getTypedResponse<List<JobsDto>>(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpJobs(@RequestParam(required = true) profileId: Long): TypedResponse<CleanUpDto> {
        val opResult = jobService.cleanUp(profileId)

        return getTypedResponse<CleanUpDto>(opResult)
    }
}