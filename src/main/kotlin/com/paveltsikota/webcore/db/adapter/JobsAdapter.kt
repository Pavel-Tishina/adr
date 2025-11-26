package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.JobsDto
import com.paveltsikota.webcore.db.entity.JobsEntity
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
                priority,
                start = Timestamp(start ?: 0),
                finish = Timestamp(finish ?: 0),
                completed,
                disabled,
                type,
                lastObject,
                lastObjectId,
                status
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
                completed,
                disabled,
                type,
                lastObject,
                lastObjectId,
                status
            )
        }
    }

}