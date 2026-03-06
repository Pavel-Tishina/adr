package com.paveltsikota.webcore.rest.model

import com.paveltsikota.webcore.db.dto.JobsTaskDto

data class PostAddManyJobsRequest(
    val jobs: List<JobsTaskDto>
)