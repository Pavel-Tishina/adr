package com.paveltsikota.webcore.service.operation

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.service.operation.enums.FileOpResultState

class FileOperationResult(
    success : Boolean = false,
    errors: ArrayList<String> = ArrayList(),
    warnings: ArrayList<String> = ArrayList(),
    override val obj: FilesEntity,
    override val result: FileOpResultState
): OperationResult(
    success = success,
    errors = errors,
    warnings = warnings
) {
}