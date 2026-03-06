package com.paveltsikota.webcore.db.service.result

import com.paveltsikota.webcore.db.dto.DuplicateDto
import com.paveltsikota.webcore.db.service.result.enums.DuplicatesOperationResultType

data class DuplicateServiceOperationResult(
    val success: Boolean = false,
    val obj: List<DuplicateDto> = emptyList(),
    val result: DuplicatesOperationResultType = DuplicatesOperationResultType.ERROR, // TODO: result as sealed class?
    val error: String? = null
)
