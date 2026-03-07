package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.utils.enums.HashType

data class DuplicateDto(
    val id: Long,
    val size: Long,
    val profile: Long,
    val hash: String,
    val hashType: HashType,
    val n: Int,
    val dupN: Int,
    var main: DuplicateFileDto? = null,
    var dups: Set<DuplicateFileDto>? = null,
): CommonDto