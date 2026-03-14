package com.paveltsikota.webcore.db.dto

import jakarta.persistence.Embeddable

@Embeddable
data class HistoryElementDto (
    val start: Long,
    var finish: Long? = null
): CommonDto