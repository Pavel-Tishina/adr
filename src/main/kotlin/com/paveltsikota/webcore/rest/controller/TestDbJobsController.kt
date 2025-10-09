package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.db.service.JobService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.rest.model.PostAddManyJobsRequest
import com.paveltsikota.webcore.rest.model.TypedResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getTypedResponse
import com.paveltsikota.webcore.utils.entity.JobEntityUtils
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobStatus.*
import com.paveltsikota.webcore.utils.enums.JobsType
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/rest/v1/test/db/jobs")
class TestDbJobsController(private val jobService: JobService) {

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
            jobService.add(profile, priority, start, finish, completed, disabled, type, lastObject, lastObjectId, status, addOnce)
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
        val opResult = jobService.update(JobEntityUtils.dtoToEntity(model))

        return getTypedResponse<List<JobsDto>>(opResult)
    }

    @DeleteMapping("/")
    fun delJob(@RequestBody model: GroupsDto): TypedResponse<List<JobsDto>> {
        val opResult = jobService.remove(model.id ?: 0)

        return getTypedResponse<List<JobsDto>>(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpJobs(@RequestParam(required = true) profileId: Long): TypedResponse<List<JobsDto>> {
        val opResult = jobService.cleanUp(profileId)

        return getTypedResponse<List<JobsDto>>(opResult)
    }


}