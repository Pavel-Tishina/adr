package com.paveltsikota.webcore.db.dto

data class ProfileDto(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val cfg: CfgDto
)