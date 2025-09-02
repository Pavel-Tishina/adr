package com.paveltsikota.webcore.rest.api

data class FilesResponse(
    val success: Boolean = false,
    val obj: List<FilesDto>? = null,
    val error: String = "",
)