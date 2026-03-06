package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.DuplicateDto
import com.paveltsikota.webcore.db.dto.DuplicateFileDto
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.utils.enums.FileState
import org.springframework.stereotype.Component
import kotlin.Long

@Component
object DuplicateHashAdapter: AbstractEntityDtoAdapter<HashesEntity, DuplicateDto>(
    entityClass = HashesEntity::class,
    dtoClass = DuplicateDto::class
) {

    override fun entityToDto(e: HashesEntity): DuplicateDto {
        return with(e) {
            DuplicateDto(id = id, size = size, profile = profile, hash = hash,
                hashType = hashType, n = e.duplicates.size, dupN = e.duplicates.size - 1)
        }
    }

    override fun dtoToEntity(dto: DuplicateDto): HashesEntity {
        return with(dto) {
            HashesEntity(
                id = id,
                profile = profile,
                size= size,
                hash = hash,
                hashType = hashType,
                main = main?.id ?: 0,
                duplicates = dups?.map{ it.id }?.toMutableSet() ?: mutableSetOf()
            )
        }
    }

}