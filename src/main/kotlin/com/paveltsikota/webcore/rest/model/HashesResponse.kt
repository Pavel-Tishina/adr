package com.paveltsikota.webcore.rest.model

import com.paveltsikota.webcore.db.dto.HashesDto

data class HashesResponse(
    val success: Boolean = false,
    val obj: List<HashesDto>? = null,
    val error: String = "",
)