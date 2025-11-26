package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.FilesDto
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.utils.FilesUtils.getFilesEntryByPathForDb
import com.paveltsikota.webcore.db.utils.FilesUtils.hasOnlyPath
import com.paveltsikota.webcore.hash.calculator.HashCalculator
import com.paveltsikota.webcore.utils.FileUtils
import org.springframework.stereotype.Component
import kotlin.io.path.Path


@Component
object FilesAdapter: AbstractEntityDtoAdapter<FilesEntity, FilesDto>(
    entityClass = FilesEntity::class,
    dtoClass = FilesDto::class
) {
    override fun entityToDto(e: FilesEntity): FilesDto {
        return with (e) {
            FilesDto(
                id, profile, size, created, modified, path, hashPath, fileName, newFileName, isUnique,
                groupId, hashId, hash, hashType, state, hold)
        }
    }

    override fun dtoToEntity(dto: FilesDto): FilesEntity {
        return dtoToEntity(dto, false, null)
    }

    fun dtoToEntity(dto: FilesDto, isLocal: Boolean = false, calc: HashCalculator? = null): FilesEntity {
        return if (isLocal && hasOnlyPath(dto)) {
            getFilesEntryByPathForDb(FileUtils.toUnixPath(Path(dto.path?: "")), calc)
        } else {
            val path = Path(dto.path?: "")
            FilesEntity(
                id = dto.id ?: 0,
                profile = dto.profile ?: 0,
                size = dto.size ?: Long.MIN_VALUE,
                created = dto.created ?: Long.MIN_VALUE,
                modified = dto.modified ?: Long.MIN_VALUE,
                path = FileUtils.toUnixPath(path),
                hashPath = dto.hashPath,
                fileName = dto.fileName ?: path.fileName.toString(),
                newFileName = dto.newFileName,
                isUnique = dto.isUnique,
                groupId = dto.groupId,
                hashId = dto.hashId,
                hash = dto.hash,
                hashType = dto.hashType,
                state = dto.state,
                hold = dto.hold == true
            )
        }
    }

}