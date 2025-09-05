package com.paveltsikota.webcore.utils

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.hash.calculator.HashCalculator
import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import com.paveltsikota.webcore.rest.api.FilesDto
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType
import java.nio.file.Path

object FilesEntityUtils {

    // Not for DB
    fun getFilesEntryByPath(path: Path): FilesEntity {
        return FilesEntity(
            path = FileUtils.toUnixPath(path),
            fileName = path.fileName.toString(),
            id = Long.MIN_VALUE,
            profile = 0,
            size = Long.MIN_VALUE,
            created = Long.MIN_VALUE,
            modified = Long.MIN_VALUE,
            hashPath = "",
            newFileName = "",
            isUnique = false,
            groupId = Long.MIN_VALUE,
            hashId = Long.MIN_VALUE,
            hash = "",
            hashType = HashType.UNKNOWN,
            state = FileState.ON_PLACE,
            hold = false,
        )
    }

    fun getFilesEntryByPathForDb(path: String, calc: HashCalculator?): FilesEntity {
        return getFilesEntryByPathForDb(Path.of(path), calc)
    }

    fun getFilesEntryByPathForDb(path: Path, calc: HashCalculator?): FilesEntity {
        val file = path.toFile()
        val exist = file.isFile
        println("file exist: $exist")
        return FilesEntity(
            path = FileUtils.toUnixPath(path),
            fileName = path.fileName.toString(),
            profile = 0,
            size = file.length(),
            created = FileUtils.getFileCreationTime(path),
            modified = FileUtils.getFileModificationTime(path),
            hashPath = if (exist && calc != null) { "/${calc.getType()}" } else { null },
            hash = if (exist && calc != null) { calc.calculate(path) } else { null },
            hashType = if (exist && calc != null) { calc.getType() } else { null },
            state = FileState.ON_PLACE.takeIf { exist }?: FileState.NOT_FOUND,
        )
    }

    fun eq(e1: FilesEntity, e2: FilesEntity): Boolean {
        return e1.id == e2.id
                && e1.state == e2.state
                && e1.hold == e2.hold
                && e1.isUnique == e2.isUnique
                && e1.profile == e2.profile
                && e1.size == e2.size
                && e1.created == e2.created
                && e1.modified == e2.modified
                && e1.hashId == e2.hashId
                && e1.groupId == e2.groupId
                && e1.hashType == e2.hashType
                && e1.fileName == e2.fileName
                && e1.newFileName == e2.newFileName
                && FileUtils.toUnixPath(Path.of(e1.path)).equals(FileUtils.toUnixPath(Path.of(e2.path)))
    }

    fun entityToDto(e: FilesEntity): FilesDto {
        return FilesDto(
            id = e.id,
            profile = e.profile,
            size = e.size,
            created = e.created,
            modified = e.modified,
            path = e.path,
            hashPath = e.hashPath,
            fileName = e.fileName,
            newFileName = e.newFileName,
            isUnique = e.isUnique,
            groupId = e.groupId,
            hashId = e.hashId,
            hash = e.hash,
            hashType = e.hashType,
            state = e.state,
            hold = e.hold
        )
    }

    fun dtoToEntity(dto: FilesDto, isLocal: Boolean = false, calc: HashCalculator? = null): FilesEntity {
        return if (isLocal && hasOnlyPath(dto)) {
            getFilesEntryByPathForDb(Path.of(dto.path), calc)
        } else {
            FilesEntity(
                id = dto.id ?: 0,
                profile = dto.profile,
                size = dto.size ?: Long.MIN_VALUE,
                created = dto.created ?: Long.MIN_VALUE,
                modified = dto.modified ?: Long.MIN_VALUE,
                path = FileUtils.toUnixPath(Path.of(dto.path)),
                hashPath = dto.hashPath,
                fileName = dto.fileName ?: Path.of(dto.path).fileName.toString(),
                newFileName = dto.newFileName,
                isUnique = dto.isUnique,
                groupId = dto.groupId,
                hashId = dto.hashId,
                hash = dto.hash,
                hashType = dto.hashType,
                state = dto.state,
                hold = dto.hold ?: false
            )
        }
    }

    fun hasOnlyPath(dto: FilesDto): Boolean {
        return dto.id == null
                && dto.hold == null
                && dto.isUnique == null
                && dto.created == null
                && dto.modified == null
                && dto.groupId == null
                && dto.hashId == null
                && dto.hashType == null
                && dto.hash.isNullOrBlank()
                && dto.hashPath.isNullOrBlank()
                && dto.fileName.isNullOrBlank()
                && dto.newFileName.isNullOrBlank()
                && dto.path.isNotBlank() // NOT BLANK
    }

    fun validateEntityForAdd(e: FilesEntity): Boolean {
        return e.fileName.isNotBlank()
                && e.path.isNotBlank()
                && e.size >= 0
                && e.profile >= 0
    }

//    fun hashNotCalculated(e: FilesEntity): Boolean {
//        return e.fileName.isNotBlank()
//                && e.path.isNotBlank()
//                && e.size >= 0
//                && e.profile >= 0
//                && e.hash.isNullOrBlank()
//                && e.hashType == null
//    }

    fun hashNotCalculated(e: FilesEntity): Boolean {
        return with (e) {
            hashNotCalculated(hash, hashType)
        }
    }

    fun hashNotCalculated(hash: String?, hashType: HashType?): Boolean {
        return hash.isNullOrBlank() || hashType == null || hashType == HashType.UNKNOWN
    }

}