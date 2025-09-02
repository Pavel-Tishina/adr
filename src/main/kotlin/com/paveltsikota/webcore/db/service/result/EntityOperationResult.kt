package com.paveltsikota.webcore.db.service.result

import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType

data class EntityOperationResult(
    val success: Boolean = false,
    val obj: Any? = null,
    val result: EntityOperationResultType = EntityOperationResultType.ERROR,
    val error: String? = null
)
