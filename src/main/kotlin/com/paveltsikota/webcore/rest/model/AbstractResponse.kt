package com.paveltsikota.webcore.rest.model

abstract class AbstractResponse(
    open val success: Boolean = false,
    open val obj: Any? = null,
    open val error: String = "",
)