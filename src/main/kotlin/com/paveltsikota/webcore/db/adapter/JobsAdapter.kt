package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.db.utils.TimeUtils.calculateRunningTime
import org.springframework.stereotype.Component
import java.sql.Timestamp

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
                uuid = uuid,
                start = start?.let { Timestamp(it) },
                finish = finish?.let { Timestamp(it) },
                global = global,
                disabled = disabled,
                taskList = taskList,
                history = history,
                status = status,
                runningTime = calculateRunningTime(history)
            )
        }
    }

    override fun dtoToEntity(dto: JobsDto): JobsEntity {
        return with(dto) {
            JobsEntity(
                id?: 0,
                profile,
                uuid = uuid,
                start = start?.time,
                finish = finish?.time,
                global = global,
                disabled = disabled,
                taskList = taskList,
                history = history,
                status = status
            )
        }
    }

}