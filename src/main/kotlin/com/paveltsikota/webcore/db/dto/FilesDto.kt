package com.paveltsikota.webcore.db.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType

data class FilesDto(
    val id: IdType?,
    val profile: ProfileType?,
    val size: SizeType?,
    val created: CreatedDateType?,
    val modified: ModifiedDateType?,
    val path: PathType?,
    @JsonProperty("hash_path")
    val hashPath: HashPathType?,
    @JsonProperty("file_name")
    val fileName: FileNameType?,
    @JsonProperty("new_file_name")
    val newFileName: NewFileNameType?,
    @JsonProperty("is_unique")
    val isUnique: IsUniqueType?,
    @JsonProperty("job_id")
    val jobId: JobIdType?,
    @JsonProperty("group_id")
    val groupId: GroupIdType?,
    @JsonProperty("hash_id")
    val hashId: HashIdType?,
    val hash: HashValueType?,
    @JsonProperty("hash_type")
    val hashType: HashType?,
    val state: FileState?,
    val hold: HoldType?
): CommonDto