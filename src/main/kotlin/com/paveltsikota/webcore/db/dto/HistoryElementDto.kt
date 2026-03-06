package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.db.types.DataTypeAlias.*

data class HistoryElementDto (
    val start: StartDateType,
    var finish: FinishDateType? = null
): CommonDto