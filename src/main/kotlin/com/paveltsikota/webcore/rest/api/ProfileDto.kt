package com.paveltsikota.webcore.rest.api

data class ProfileDto(
    val id: Long = 0,
    val title: String,
    val description: String?,
    val cfg: CfgDto
)