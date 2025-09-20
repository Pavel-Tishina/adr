package com.paveltsikota.webcore.rest.model

import com.paveltsikota.webcore.db.dto.JobsDto

data class JobsResponse(
    val success: Boolean = false,
    val obj: List<JobsDto>? = null,
    val error: String = "",
)