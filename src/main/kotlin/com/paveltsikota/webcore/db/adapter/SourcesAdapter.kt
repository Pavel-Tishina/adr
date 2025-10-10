package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.SourcesDto
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.utils.FileUtils
import org.springframework.stereotype.Component
import kotlin.io.path.Path

@Component
object SourcesAdapter: AbstractEntityDtoAdapter<SourcesEntity, SourcesDto>(
    entityClass = SourcesEntity::class.java,
    dtoClass = SourcesDto::class.java
) {

    override fun eqEntity(e1: SourcesEntity, e2: SourcesEntity): Boolean {
        return e1.id == e2.id
                && e1.profile == e2.profile
                && e1.dirorder == e2.dirorder
                && e1.path == e2.path
    }

    override fun eqDto(dto1: SourcesDto, dto2: SourcesDto): Boolean {
        return dto1 == dto2
    }

    override fun entityToDto(e: SourcesEntity): SourcesDto {
        return SourcesDto(id = e.id, profile = e.profile, dirorder = e.dirorder, path = e.path)
    }

    override fun dtoToEntity(dto: SourcesDto): SourcesEntity {
        return with(dto) {
            SourcesEntity(id = id?: 0, profile = profile, dirorder = dirorder, path = FileUtils.toUnixPath(Path(path)))
        }
    }

}