package com.paveltsikota.webcore.db.dto

data class DuplicateDto(
    val id: Long,
    val size: Long,
    val hash: String,
    val hashType: String,
    val n: Int,
    val dupN: Int,
    var main: DuplicateFileDto,
    var dups: Set<DuplicateFileDto>
)