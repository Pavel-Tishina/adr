package com.paveltsikota.webcore.db.utils

import com.paveltsikota.webcore.db.dto.FilesDto
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.hash.calculator.HashCalculator
import com.paveltsikota.webcore.utils.FileUtils
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType
import org.apache.commons.lang3.ObjectUtils.allNull
import java.nio.file.Path
import kotlin.io.path.Path

object FilesUtils {

    fun hasOnlyPath(dto: FilesDto): Boolean {
        return with (dto) {
            allNull(id, hold, isUnique, created, modified, groupId, hashId, hashType)
               && hash.isNullOrBlank()
               && hashPath.isNullOrBlank()
               && fileName.isNullOrBlank()
               && newFileName.isNullOrBlank()
               && !dto.path.isNullOrBlank() // NOT BLANK
        }
    }

    // Not for DB
    fun getFilesEntryForFilesOperationResult(path: Path, profile: Long = DEFAULT_PROFILE): FilesEntity {
        return FilesEntity(
            path = FileUtils.toUnixPath(path),
            fileName = path.fileName.toString(),
            id = Long.MIN_VALUE,
            profile = profile.takeIf { profile > 0 } ?: DEFAULT_PROFILE,
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
        return getFilesEntryByPathForDb(Path(path), calc)
    }

    fun getFilesEntryByPathForDb(path: Path, calc: HashCalculator?): FilesEntity {
        val file = path.toFile()
        val exist = file.isFile
        println("file exist: $exist") // debug
        return FilesEntity(
            path = FileUtils.toUnixPath(path),
            fileName = path.fileName.toString(),
            profile = 0,
            size = file.length(),
            created = FileUtils.getFileCreationTime(path),
            modified = FileUtils.getFileModificationTime(path),
            hashPath = if (exist && calc != null) { "/${calc.getType()}" } else { null },
            hash = if (exist && calc != null) {
                calc.calculate(path)
            } else {
                null
            },
            hashType = if (exist && calc != null) {
                calc.getType()
            } else {
                null
            },
            state = FileState.ON_PLACE.takeIf { exist } ?: FileState.NOT_FOUND,
        )
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
        return with (e) { hashNotCalculated(hash, hashType) }
    }

    fun hashNotCalculated(hash: String?, hashType: HashType?): Boolean {
        return hash.isNullOrBlank() || hashType == null || hashType == HashType.UNKNOWN
    }

}