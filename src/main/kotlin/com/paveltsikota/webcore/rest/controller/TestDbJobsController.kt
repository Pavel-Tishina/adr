package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.service.JobService
import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.rest.model.GetFilesByIdsRequest
import com.paveltsikota.webcore.rest.model.JobsResponse
import com.paveltsikota.webcore.rest.model.PostAddManyJobsRequest
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getJobsResponse
import com.paveltsikota.webcore.service.operation.OperationResult
import com.paveltsikota.webcore.utils.entity.GroupsEntityUtils
import com.paveltsikota.webcore.utils.entity.JobEntityUtils
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobStatus.CREATED
import com.paveltsikota.webcore.utils.enums.JobStatus.RUNNING
import com.paveltsikota.webcore.utils.enums.JobStatus.PAUSED
import com.paveltsikota.webcore.utils.enums.JobsType
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/rest/v1/test/db/jobs")
class TestDbJobsController(private val jobService: JobService) {

    @GetMapping("/get/{id}")
    fun getJobById(@PathVariable id: Long): JobsResponse {
        val opResult = jobService.getById(id)

        return getJobsResponse(opResult)
    }

    @GetMapping("/get")
    fun getJobs(
        @RequestParam(required = false) profileId: Long?,
        @RequestParam(required = false) priority: Int?,
        @RequestParam(required = false) type: JobsType?,
        @RequestParam(required = false) status: JobStatus?
    ): JobsResponse {
        val opResult = jobService.get(profileId, priority, type, status)

        return getJobsResponse(opResult)
    }

    @GetMapping("/get-by-status")
    fun getByStatus(
        @RequestParam(required = false) profileId: Long?,
        @RequestParam(required = false) status: JobStatus
    ): JobsResponse {
        val opResult = when (status) {
            CREATED -> jobService.getAllNotStarted(profileId)
            RUNNING -> jobService.getAllRun(profileId)
            PAUSED -> jobService.getAllPaused(profileId)
            else -> null
        }

        val finalResult = EntityOperationResult(success = true, obj = opResult, result = EntityOperationResultType.OK)
            .takeIf { opResult != null }
            ?: EntityOperationResult(success = false, error = "Wrong status set", result = EntityOperationResultType.ERROR)

        return getJobsResponse(finalResult)
    }

    @PostMapping("/")
    fun addJob(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: JobsDto
    ): JobsResponse {
        val opResult = with(model) {
            jobService.add(profile, priority, start, finish, completed, disabled, type, lastObject, lastObjectId, status, addOnce)
        }

        return getJobsResponse(opResult)
    }

    @PostMapping("/many")
    fun addManyJobs(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: PostAddManyJobsRequest
    ): JobsResponse {
        val opResult = jobService.addDto(model.jobs, addOnce)

        return getJobsResponse(opResult)
    }

    @PutMapping("/")
    fun updJob(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: JobsDto
    ): JobsResponse {
        val opResult = jobService.update(JobEntityUtils.dtoToEntity(model))

        return getJobsResponse(opResult)
    }

    @DeleteMapping("/")
    fun delJob(@RequestBody model: GroupsDto): JobsResponse {
        val opResult = jobService.remove(model.id ?: 0)

        return getJobsResponse(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpGroups(@RequestParam(required = true) profileId: Long): JobsResponse {
        val opResult = jobService.cleanUp(profileId)

        return getJobsResponse(opResult)
    }


}