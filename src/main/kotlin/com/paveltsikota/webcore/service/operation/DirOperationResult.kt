package com.paveltsikota.webcore.service.operation

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.service.operation.enums.DirOpResultState
import com.paveltsikota.webcore.service.operation.enums.FileOpResultState
import java.nio.file.Path

class DirOperationResult(
    success : Boolean = false,
    errors: ArrayList<String> = ArrayList(),
    warnings: ArrayList<String> = ArrayList(),
    obj: Path,
    result: DirOpResultState,
): OperationResult(
    success = success,
    errors = errors,
    warnings = warnings
) {
    override val obj = obj
    override val result = result
}