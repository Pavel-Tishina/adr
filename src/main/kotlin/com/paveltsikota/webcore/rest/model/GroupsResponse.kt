package com.paveltsikota.webcore.rest.model

import com.paveltsikota.webcore.db.dto.GroupsDto

data class GroupsResponse(
    val success: Boolean = false,
    val obj: List<GroupsDto>? = null,
    val error: String = "",
)