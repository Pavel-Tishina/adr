package com.paveltsikota.webcore.db.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CleanUpDto(
    @JsonProperty("removed_objects")
    val removedObjects: Long = 0L
): CommonDto
