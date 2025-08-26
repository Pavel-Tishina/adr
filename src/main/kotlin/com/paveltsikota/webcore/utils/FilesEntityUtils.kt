package com.paveltsikota.webcore.utils

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.io.path.getLastModifiedTime

object FilesEntityUtils {

    // Not for DB
    fun getFilesEntryByPath(path: Path): FilesEntity {
        return FilesEntity(
            path = FileUtils.toUnixPath(path),
            fileName = path.fileName.toString(),
            id = Long.MIN_VALUE,
            profile = Long.MIN_VALUE,
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

}