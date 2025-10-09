package com.paveltsikota.webcore.utils.entity

import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.db.dto.JobsDto
import java.sql.Timestamp

object JobEntityUtils {

    fun eq(e1: JobsEntity, e2: JobsEntity): Boolean {
        return e1.id == e2.id
                && e1.profile == e2.profile
                && e1.priority == e2.priority
                && e1.type == e2.type
                && e1.status == e2.status
                && e1.start == e2.start
                && e1.finish == e2.finish
                && e1.completed == e2.completed
                && e1.disabled == e2.disabled
                && e1.lastObjectId == e2.lastObjectId
                && e1.lastObject == e2.lastObject
    }

    fun entityToDto(e: JobsEntity): JobsDto {
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

    fun dtoToEntity(dto: JobsDto): JobsEntity {
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