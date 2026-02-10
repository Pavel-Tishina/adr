package com.paveltsikota.webcore.db.dto.filter

import com.paveltsikota.webcore.db.dto.filter.enums.FilterExpression
import com.paveltsikota.webcore.db.dto.filter.enums.FilterOperation

data class FilterItemDto(
    val not: Boolean? = false,
    val preExpression: FilterExpression? = null,
    val op: FilterOperation,
    val item: FilterItemDto,
    val postExpression: FilterExpression? = null,
)