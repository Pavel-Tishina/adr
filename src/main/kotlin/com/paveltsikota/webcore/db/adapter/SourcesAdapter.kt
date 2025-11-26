package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.SourcesDto
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.utils.FileUtils
import org.springframework.stereotype.Component
import kotlin.io.path.Path

@Component
object SourcesAdapter: AbstractEntityDtoAdapter<SourcesEntity, SourcesDto>(
    entityClass = SourcesEntity::class,
    dtoClass = SourcesDto::class
) {

    override fun entityToDto(e: SourcesEntity): SourcesDto {
        return with(e) { SourcesDto(id, profile, dirorder, path) }
    }

    override fun dtoToEntity(dto: SourcesDto): SourcesEntity {
        return with(dto) { SourcesEntity(id?: 0, profile, dirorder, FileUtils.toUnixPath(Path(path))) }
    }

}