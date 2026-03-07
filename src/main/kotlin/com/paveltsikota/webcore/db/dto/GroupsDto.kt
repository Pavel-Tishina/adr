package com.paveltsikota.webcore.db.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupsDto(
    val id: Long?,
    val profile: Long,
    val size: Long,
    val fileIds: Set<Long>?,
    @JsonProperty("job_id")
    val jobId: Long?,
): CommonDto