package com.paveltsikota.webcore.rest.api

data class GroupsDto(
    val id: Long?,
    val profile: Long,
    val size: Long,
    val fileIds: Set<Long>?,
)