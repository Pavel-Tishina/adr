package com.paveltsikota.webcore.db.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType

data class FilesDto(
    val id: Long?,
    val profile: Long?,
    val size: Long?,
    val created: Long?,
    val modified: Long?,
    val path: String?,
    @JsonProperty("hash_path")
    val hashPath: String?,
    @JsonProperty("file_name")
    val fileName: String?,
    @JsonProperty("new_file_name")
    val newFileName: String?,
    @JsonProperty("is_unique")
    val isUnique: IsUniqueType?,
    @JsonProperty("job_id")
    val jobId: JobIdType?,
    @JsonProperty("group_id")
    val groupId: Long?,
    @JsonProperty("hash_id")
    val hashId: Long?,
    val hash: String?,
    @JsonProperty("hash_type")
    val hashType: HashType?,
    val state: FileState?,
    val hold: Boolean?
): CommonDto