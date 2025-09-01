package com.paveltsikota.webcore.rest.api

import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType

data class FilesDto(
    val id: Long,
    val profile: Long,
    val size: Long,
    val created: Long,
    val modified: Long,
    val path: String,
    val hashPath: String?,
    val fileName: String,
    val newFileName: String?,
    val isUnique: Boolean?,
    val groupId: Long?,
    val hashId: Long?,
    val hash: String?,
    val hashType: HashType?,
    val state: FileState?,
    val hold: Boolean
)