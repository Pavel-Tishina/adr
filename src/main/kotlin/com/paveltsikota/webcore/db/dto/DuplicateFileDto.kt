package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.enums.FileState

class DuplicateFileDto(
    val id: IdType,
    val created: CreatedDateType,
    val modified: ModifiedDateType,
    val path: PathType,
    val state: FileState,
    val hold: HoldType
): CommonDto