package com.paveltsikota.webcore.db.dto

data class HistoryElementDto (
    val start: Long,
    var end: Long? = null
): CommonDto