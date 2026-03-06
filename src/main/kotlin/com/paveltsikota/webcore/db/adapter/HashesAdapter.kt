package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.HashesDto
import com.paveltsikota.webcore.db.entity.HashesEntity
import org.springframework.stereotype.Component

@Component
object HashesAdapter: AbstractEntityDtoAdapter<HashesEntity, HashesDto>(
    entityClass = HashesEntity::class,
    dtoClass = HashesDto::class
) {
    override fun entityToDto(e: HashesEntity): HashesDto {
        return with(e) { HashesDto(id, size, profile, hash, hashType, main, duplicates, jobId) }
    }

    override fun dtoToEntity(dto: HashesDto): HashesEntity {
        return with(dto) {
            HashesEntity(id ?: 0, profile, size, hash, hashType, main, duplicates as MutableSet<Long>, jobId)
        }
    }

}