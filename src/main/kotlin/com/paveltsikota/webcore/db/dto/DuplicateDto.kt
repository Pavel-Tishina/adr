package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.enums.HashType

data class DuplicateDto(
    val id: IdType,
    val size: SizeType,
    val profile: ProfileType,
    val hash: HashValueType,
    val hashType: HashType,
    val n: NType,
    val dupN: DupNType,
    var main: DuplicateFileDto? = null,
    var dups: Set<DuplicateFileDto>? = null,
): CommonDto