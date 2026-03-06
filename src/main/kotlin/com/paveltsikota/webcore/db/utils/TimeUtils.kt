package com.paveltsikota.webcore.db.utils

import com.paveltsikota.webcore.db.dto.HistoryElementDto
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TimeUtils {
    fun calculateRunningTime(history: MutableList<HistoryElementDto>?): String? {
        return history?.let {
            val now = System.nanoTime()
            durationToRunningTime(
                history
                    .sumOf { (start, end) -> ((end ?: now) - start) }
                    .toDuration(DurationUnit.NANOSECONDS)
            )
        }
    }

    // REFACTOR IT!
    fun durationToRunningTime(duration: Duration): String {
        val days = duration.toLong(DurationUnit.DAYS)
        val hours = duration.toLong(DurationUnit.HOURS) % 24
        val minutes = duration.toLong(DurationUnit.MINUTES) % 60
        val seconds = duration.toLong(DurationUnit.SECONDS) % 360

        return "Operation take: $days days $hours hours $minutes minutes $seconds seconds"
    }
}