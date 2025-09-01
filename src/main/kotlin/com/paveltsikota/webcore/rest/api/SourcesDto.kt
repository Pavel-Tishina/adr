package com.paveltsikota.webcore.rest.api

data class SourcesDto(
    val id: Long = 0,
    val profile: Long,
    val dirorder: Int,
    val path: String,
)