package com.paveltsikota.webcore.rest.model

import com.paveltsikota.webcore.db.dto.SourcesDto

data class ManySourcesRequest(
    val sources: List<SourcesDto>
)