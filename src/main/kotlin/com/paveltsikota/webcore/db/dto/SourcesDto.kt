package com.paveltsikota.webcore.db.dto

data class SourcesDto(
    val id: Long? = 0,
    val profile: Long,
    val dirorder: Int,
    val path: String,
)