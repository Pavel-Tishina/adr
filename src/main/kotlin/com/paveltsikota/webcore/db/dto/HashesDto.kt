package com.paveltsikota.webcore.db.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.enums.HashType

data class HashesDto(
    val id: IdType?,
    val profile: ProfileType,
    val size: SizeType,
    val hash: HashValueType,
    val hashType: HashType,
    val main: MainType,
    val duplicates: Set<Long>?,
    @JsonProperty("job_id")
    val jobId: JobIdType?,
): CommonDto