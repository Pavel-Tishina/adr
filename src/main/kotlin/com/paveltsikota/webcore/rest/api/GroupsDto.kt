package com.paveltsikota.webcore.rest.api

data class GroupsDto(
    val id: Long = 0,
    val profile: Long,
    val size: Long,
    val fileIds: Set<Long>,
)