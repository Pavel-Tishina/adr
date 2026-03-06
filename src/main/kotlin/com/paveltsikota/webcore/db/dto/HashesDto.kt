package com.paveltsikota.webcore.db.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.enums.HashType

data class HashesDto(
    val id: Long?,
    val profile: Long,
    val size: Long,
    val hash: String,
    val hashType: HashType,
    val main: Long,
    val duplicates: Set<Long>?,
    @JsonProperty("job_id")
    val jobId: JobIdType?,
): CommonDto