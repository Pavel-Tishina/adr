package com.paveltsikota.webcore.rest.api

import com.paveltsikota.webcore.utils.enums.HashType

data class CfgDto(
    val hashDir: String,
    val hashType: HashType,
    val bufferSize: Long,
    val progressN: Int,
    val progressSize: Long,
    val progressShow: Boolean,
    val flyHashCalculate: Boolean,
)