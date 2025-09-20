package com.paveltsikota.webcore.db.dto

data class GroupsDto(
    val id: Long?,
    val profile: Long,
    val size: Long,
    val fileIds: Set<Long>?,
)