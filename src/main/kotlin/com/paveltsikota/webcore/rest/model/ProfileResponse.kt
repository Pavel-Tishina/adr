package com.paveltsikota.webcore.rest.model

import com.paveltsikota.webcore.db.dto.ProfileDto

data class ProfileResponse(
    val success: Boolean = false,
    val obj: List<ProfileDto>? = null,
    val error: String = "",
)