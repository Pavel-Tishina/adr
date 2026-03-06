package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.db.types.DataTypeAlias.*

data class ProfileDto(
    val id: IdType? = 0,
    val title: TitleType,
    val description: DescriptionType?,
    val cfg: CfgDto
): CommonDto