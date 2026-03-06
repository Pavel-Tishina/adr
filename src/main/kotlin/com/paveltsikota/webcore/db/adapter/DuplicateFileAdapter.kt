package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.DuplicateFileDto
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.utils.enums.FileState
import org.springframework.stereotype.Component

@Component
object DuplicateFileAdapter: AbstractEntityDtoAdapter<FilesEntity, DuplicateFileDto>(
    entityClass = FilesEntity::class,
    dtoClass = DuplicateFileDto::class
) {

    override fun entityToDto(e: FilesEntity): DuplicateFileDto {
        return with(e) {
            DuplicateFileDto(id, created, modified, path, state ?: FileState.ON_PLACE, hold)
        }
    }

    override fun dtoToEntity(dto: DuplicateFileDto): FilesEntity {
        return with(dto) {
            FilesEntity(
                id = id,
                created = created,
                modified = modified,
                path = path,
                state = state,
                hold = hold,
            )
        }
    }

}