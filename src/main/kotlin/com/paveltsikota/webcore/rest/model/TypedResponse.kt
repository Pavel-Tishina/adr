package com.paveltsikota.webcore.rest.model

data class TypedResponse<T>(
    override val success: Boolean,
    override val obj: T?,
    override val error: String
) : AbstractResponse()