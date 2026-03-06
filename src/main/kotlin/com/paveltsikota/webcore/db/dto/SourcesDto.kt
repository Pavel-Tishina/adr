package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.db.types.DataTypeAlias.*

data class SourcesDto(
    val id: IdType? = 0,
    val profile: ProfileType,
    val dirorder: DirOrderType,
    val path: PathType,
): CommonDto