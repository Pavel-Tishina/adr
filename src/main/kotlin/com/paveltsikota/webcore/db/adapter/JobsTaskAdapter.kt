package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.JobsTaskDto
import com.paveltsikota.webcore.db.entity.JobsTaskEntity
import com.paveltsikota.webcore.db.utils.TimeUtils.calculateRunningTime
import org.springframework.stereotype.Component
import java.sql.Timestamp

@Component
object JobsTaskAdapter: AbstractEntityDtoAdapter<JobsTaskEntity, JobsTaskDto>(
    entityClass = JobsTaskEntity::class,
    dtoClass = JobsTaskDto::class
) {

    override fun entityToDto(e: JobsTaskEntity): JobsTaskDto {
        return with(e) {
            JobsTaskDto(
                id,
                profile,
                priority,
                jobId,
                start = Timestamp(start ?: 0),
                finish = Timestamp(finish ?: 0),
                disabled,
                type,
                lastObjectId,
                objects,
                history,
                status,
                runningTime = calculateRunningTime(history)
            )
        }
    }

    override fun dtoToEntity(dto: JobsTaskDto): JobsTaskEntity {
        return with(dto) {
            JobsTaskEntity(
                id?: 0,
                profile,
                priority?: 0,
                start = start?.time ?: 0,
                finish = finish?.time ?: 0,
                disabled,
                type,
                lastObjectId,
                objects,
                history,
                status
            )
        }
    }

}