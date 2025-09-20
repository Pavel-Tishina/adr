package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.utils.enums.HashType

data class HashesDto(
    val id: Long?,
    val profile: Long,
    val size: Long,
    val hash: String,
    val hashType: HashType,
    val main: Long,
    val duplicates: Set<Long>?,
)