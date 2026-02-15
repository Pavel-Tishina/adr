package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.HistoryElementDto
import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.db.entity.JobsEntity
import liquibase.logging.mdc.customobjects.History
import org.springframework.stereotype.Component
import java.sql.Timestamp
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Component
object JobsAdapter: AbstractEntityDtoAdapter<JobsEntity, JobsDto>(
    entityClass = JobsEntity::class,
    dtoClass = JobsDto::class
) {

    override fun entityToDto(e: JobsEntity): JobsDto {
        return with(e) {
            JobsDto(
                id,
                profile,
                priority,
                start = Timestamp(start ?: 0),
                finish = Timestamp(finish ?: 0),
                disabled,
                type,
                lastObject,
                lastObjectId,
                objects,
                history,
                status,
                runningTime = calculateRunningTime(history)
            )
        }
    }

    override fun dtoToEntity(dto: JobsDto): JobsEntity {
        return with(dto) {
            JobsEntity(
                id?: 0,
                profile,
                priority?: 0,
                start = start?.time ?: 0,
                finish = finish?.time ?: 0,
                disabled,
                type,
                lastObject,
                lastObjectId,
                objects,
                history,
                status
            )
        }
    }

    private fun calculateRunningTime(history: MutableList<HistoryElementDto>?): String? {
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
    private fun durationToRunningTime(duration: Duration): String {
        val days = duration.toLong(DurationUnit.DAYS)
        val hours = duration.toLong(DurationUnit.HOURS) % 24
        val minutes = duration.toLong(DurationUnit.MINUTES) % 60
        val seconds = duration.toLong(DurationUnit.SECONDS) % 360

        return "Operation take: $days days $hours hours $minutes minutes $seconds seconds"
    }

}