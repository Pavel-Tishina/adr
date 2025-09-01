package com.paveltsikota.webcore.rest.api

import com.paveltsikota.webcore.utils.enums.HashType

data class HashesDto(
    val id: Long = 0,
    val profile: Long,
    val size: Long,
    val hash: String,
    val hashType: HashType,
    val main: Long,
    val duplicates: Set<Long>,
)