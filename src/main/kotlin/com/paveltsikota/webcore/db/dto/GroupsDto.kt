package com.paveltsikota.webcore.db.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.paveltsikota.webcore.db.types.DataTypeAlias.*

data class GroupsDto(
    val id: IdType?,
    val profile: ProfileType,
    val size: SizeType,
    val fileIds: Set<Long>?,
    @JsonProperty("job_id")
    val jobId: JobIdType?,
): CommonDto