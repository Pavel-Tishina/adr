package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.utils.enums.FileState

class DuplicateFileDto(
    val id: Long,
    val created: Long,
    val modified: Long,
    val path: String,
    val state: FileState,
    val hold: Boolean
)