package com.paveltsikota.webcore.service.operation

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.service.operation.enums.CommonOpResultState
import com.paveltsikota.webcore.service.operation.enums.FileOpResultState

class CommonOperationResult(
    success : Boolean = false,
    errors: ArrayList<String> = ArrayList(),
    warnings: ArrayList<String> = ArrayList(),
    override val result: CommonOpResultState
): OperationResult(
    success = success,
    errors = errors,
    warnings = warnings
) {
    override val obj = null
}