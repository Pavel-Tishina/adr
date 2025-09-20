package com.paveltsikota.webcore.rest.model

import com.paveltsikota.webcore.db.dto.FilesDto

data class FilesResponse(
    val success: Boolean = false,
    val obj: List<FilesDto>? = null,
    val error: String = "",
)