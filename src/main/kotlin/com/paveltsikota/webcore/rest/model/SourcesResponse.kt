package com.paveltsikota.webcore.rest.model

import com.paveltsikota.webcore.db.dto.SourcesDto

data class SourcesResponse(
    val success: Boolean = false,
    val obj: List<SourcesDto>? = null,
    val error: String = "",
)