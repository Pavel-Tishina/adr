package com.paveltsikota.webcore.rest.model

data class CommonResponse(
    override val success: Boolean = false,
    override val obj: List<*>? = null,
    override val error: String = "",
) : AbstractResponse()