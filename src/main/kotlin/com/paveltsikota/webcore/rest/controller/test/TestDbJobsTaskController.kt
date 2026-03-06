package com.paveltsikota.webcore.rest.controller.test

import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.dto.JobsTaskDto
import com.paveltsikota.webcore.db.service.JobTaskService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.rest.model.PostAddManyJobsRequest
import com.paveltsikota.webcore.rest.model.TypedResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getTypedResponse
import com.paveltsikota.webcore.db.adapter.JobsTaskAdapter
import com.paveltsikota.webcore.db.dto.CleanUpDto
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobStatus.*
import com.paveltsikota.webcore.utils.enums.JobsType
import org.springframework.web.bind.annotation.*

@Deprecated(message = "Only 4 testing")
@RestController
@RequestMapping("/rest/v1/test/db/jobs")
class TestDbJobsTaskController(private val jobService: JobTaskService) {

    @GetMapping("/get/{id}")
    fun getJobById(@PathVariable id: Long): TypedResponse<List<JobsTaskDto>> {
        val opResult = jobService.getById(id)

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @GetMapping("/get")
    fun getJobs(
        @RequestParam(required = false) profileId: Long?,
        @RequestParam(required = false) priority: Int?,
        @RequestParam(required = false) type: JobsType?,
        @RequestParam(required = false) status: JobStatus?
    ): TypedResponse<List<JobsTaskDto>> {
        val opResult = jobService.get(profileId, priority, type, status)

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @GetMapping("/get-by-status")
    fun getJobByStatus(
        @RequestParam(required = false) profileId: Long,
        @RequestParam(required = false) status: JobStatus
    ): TypedResponse<List<JobsTaskDto>> {
        val opResult = when (status) {
            CREATED -> jobService.getAllNotStarted(profileId)
            RUNNING -> jobService.getAllRun(profileId)
            PAUSED -> jobService.getAllPaused(profileId)
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
            jobService.add(profile, priority, jobId, start, finish, disabled, type, lastObjectId, status, objects, history, addOnce)
        }

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @PostMapping("/many")
    fun addManyJobs(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: PostAddManyJobsRequest
    ): TypedResponse<List<JobsTaskDto>> {
        val opResult = jobService.addDto(model.jobs, addOnce)

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @PutMapping("/")
    fun updJob(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: JobsTaskDto
    ): TypedResponse<List<JobsTaskDto>> {
        val opResult = jobService.update(JobsTaskAdapter.dtoToEntity(model))

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @DeleteMapping("/")
    fun delJob(@RequestBody model: GroupsDto): TypedResponse<List<JobsTaskDto>> {
        val opResult = jobService.remove(model.id ?: 0)

        return getTypedResponse<List<JobsTaskDto>>(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpJobs(@RequestParam(required = true) profileId: Long): TypedResponse<CleanUpDto> {
        val opResult = jobService.cleanUp(profileId)

        return getTypedResponse<CleanUpDto>(opResult)
    }


}