package com.paveltsikota.webcore.rest.model

import com.paveltsikota.webcore.db.dto.JobsDto

data class PostAddManyJobsRequest(
    val jobs: List<JobsDto>
)