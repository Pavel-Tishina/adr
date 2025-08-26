package com.paveltsikota.webcore.utils

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.service.operation.CommonOperationResult
import com.paveltsikota.webcore.service.operation.DirOperationResult
import com.paveltsikota.webcore.service.operation.FileOperationResult
import com.paveltsikota.webcore.service.operation.OperationResult
import com.paveltsikota.webcore.service.operation.enums.CommonOpResultState
import com.paveltsikota.webcore.service.operation.enums.DirOpResultState
import com.paveltsikota.webcore.service.operation.enums.FileOpResultState
import com.paveltsikota.webcore.utils.FilesEntityUtils.getFilesEntryByPath
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.ERROR_IO
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.MOVE_TO_HASH_DIR_ERROR_FILE_NOT_EXIST
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.MOVE_TO_HASH_DIR_ERROR_SAME_PATH
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.MOVE_TO_HASH_DIR_ERROR_WRONG_STATUS
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.RESTORE_ERROR_ORIGINAL_FILE_NOT_EXIST
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.RESTORE_ERROR_RESTORED_FILE_ALREADY_EXIST
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.RESTORE_ERROR_SAME_PATH
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.RESTORE_ERROR_WRONG_STATUS
import com.paveltsikota.webcore.utils.errors.FileOperationWarnings.WARN_FILE_RENAMED
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import java.util.*
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively
import kotlin.io.path.writeText

object FileUtils {
    val WIN_FOLDER_DELIM_PATTERN = Regex("\\\\", RegexOption.DOT_MATCHES_ALL)
    val UNIX_FOLDER_DELIM = "/"
    val JAVA_TMP_DIR = "java.io.tmpdir"

    val CREATION_TIME_ATTR = "creationTime"

    val FILE_NAME_PATTERN = Regex("""(.*?)\.(.*?)""")

    // TODO: clean-up
//    fun computeFileHash(path: Path, algorithm: String = "SHA-256"): ByteArray {
//        val hasher = MessageDigest.getInstance(algorithm)
//        Files.newByteChannel(path).use { channel ->
//            val buffer = ByteBuffer.allocateDirect(8 shl 20)
//            while (channel.read(buffer) != -1) {
//                buffer.flip().also(hasher::update).clear()
//            }
//        }
//        return hasher.digest()
//    }

    // TODO: return FileOperationResult
    fun copyFile(source: Path, dest: Path): File? {
        val destFile = dest.toFile()
        val os = FileOutputStream(destFile)
        var error = false

        try {
            Files.copy(source, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            error = true
        }

        return if (error) { null } else { destFile }
    }

    fun restoreFile(savedCopy: Path, restoreFile: FilesEntity): FileOperationResult {
        val restorePath = Path.of(restoreFile.path)

        return if (restoreFile.state != FileState.DELETED || !Files.exists(savedCopy) || Files.exists(restorePath) || savedCopy == restorePath) {
            val errors = ArrayList<String>()

            if (restoreFile.state != FileState.DELETED) { errors.add(RESTORE_ERROR_WRONG_STATUS) }
            if (!Files.exists(savedCopy)) { errors.add(RESTORE_ERROR_ORIGINAL_FILE_NOT_EXIST) }
            if (savedCopy != restorePath && Files.exists(restorePath)) { errors.add(RESTORE_ERROR_RESTORED_FILE_ALREADY_EXIST) }
            if (savedCopy == restorePath && Files.exists(savedCopy)) { errors.add(RESTORE_ERROR_SAME_PATH) }

            FileOperationResult(success = false, errors = errors, obj = restoreFile, result = FileOpResultState.FILE_NOT_RESTORED)
        } else if (copyFile(savedCopy, restorePath) != null) {
            Files.setAttribute(restorePath, CREATION_TIME_ATTR, FileTime.fromMillis(restoreFile.created))
            Files.setLastModifiedTime(restorePath, FileTime.fromMillis(restoreFile.modified))

            restoreFile.state = FileState.ON_PLACE
            FileOperationResult(success = true, obj = restoreFile, result = FileOpResultState.FILE_RESTORED)
        } else {
            FileOperationResult(success = false, errors = arrayListOf(ERROR_IO), obj = restoreFile, result = FileOpResultState.FILE_NOT_RESTORED)
        }
    }

    fun createFile(content: String, path: Path, mkDirs: Boolean = false): Boolean {
        return try {
            if (mkDirs) { File(path.parent.toString()).mkdirs() }
            Files.createFile(path).writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun copyFileToHashDir(file: FilesEntity, rootHashDir: Path): FileOperationResult {
        val hashDir = Path.of(rootHashDir.toString(), file.hash)
        val sourceFile = Path.of(file.path)
        val sourceDir = sourceFile.parent

        return if (file.state != FileState.MARK_MOVE || !Files.exists(sourceFile) || hashDir == sourceDir) {
            val errors = ArrayList<String>()

            if (file.state != FileState.MARK_MOVE) { errors.add(MOVE_TO_HASH_DIR_ERROR_WRONG_STATUS) }
            if (!Files.exists(sourceFile)) { errors.add(MOVE_TO_HASH_DIR_ERROR_FILE_NOT_EXIST) }
            if (hashDir == sourceDir) { errors.add(MOVE_TO_HASH_DIR_ERROR_SAME_PATH) }

            FileOperationResult(success = false, errors = errors, obj = file, result = FileOpResultState.FILE_NOT_MOVED)
        } else {
            File(hashDir.toString()).mkdirs()
            val destFileName = getNewFileName(file.fileName).takeIf {Files.exists(Path.of(hashDir.toString(), file.fileName)) }?: file.fileName

            val destFile = Path.of(hashDir.toString(), destFileName)

            if (copyFile(sourceFile, destFile) != null) {
                val warn = ArrayList<String>()
                if (destFileName != file.fileName) {
                    file.newFileName = destFileName
                    warn.add("$WARN_FILE_RENAMED $destFileName")
                }
                file.hashPath = toUnixPath(hashDir)
                file.state = FileState.MOVED

                FileOperationResult(success = true, warnings = warn, obj = file, result = FileOpResultState.FILE_MOVED)
            } else {
                FileOperationResult(
                    success = false, errors = arrayListOf(MOVE_TO_HASH_DIR_ERROR_WRONG_STATUS), obj = file, result = FileOpResultState.FILE_NOT_MOVED)
            }
        }
    }

    fun getTempDir(): Path {
        val tmp = System.getProperty(JAVA_TMP_DIR)
        return Path.of(tmp)
    }

    fun toUnixPath(path: Path): String {
        return path.toString().replace(WIN_FOLDER_DELIM_PATTERN, UNIX_FOLDER_DELIM)
    }

    // USE IT CAREFULLY
    // TODO: strings to const
    @OptIn(ExperimentalPathApi::class)
    fun deleteDirOrFile(path: Path): OperationResult {
        val fileOrDir = File(path.toString())
        return if (fileOrDir.isDirectory) {
            val nestedObjects = fileOrDir.listFiles()
            val hasNestedObjects = nestedObjects?.isNotEmpty() ?: false
            val warnings = ArrayList<String>()
            val errors = ArrayList<String>()
            var success = false

            if (hasNestedObjects) {
                warnings.add("Next object(s) shall delete too:")
                if (nestedObjects != null) {
                    warnings.addAll(nestedObjects.map { it.name }.toList())
                }
            }
            try {
                path.deleteRecursively()
                success = true
            } catch (e: Exception) {
                errors.add("Some objects was not deleted:")
                errors.add(e.message?: "no details")
            }

            val result = if (success) {
                DirOpResultState.DIR_WITH_NESTED_OBJECT_DELETED.takeIf { hasNestedObjects }?: DirOpResultState.DIR_DELETED
            } else {
                DirOpResultState.DIR_WITH_NESTED_OBJECT_NOT_DELETED.takeIf { hasNestedObjects }?: DirOpResultState.DIR_NOT_DELETED
            }

            DirOperationResult(success = success, warnings = warnings, errors = errors, obj = path, result = result)
        } else if (fileOrDir.isFile) {
            val success = Files.deleteIfExists(path)
            val result = FileOpResultState.FILE_DELETED.takeIf { success }?: FileOpResultState.FILE_NOT_DELETED

            FileOperationResult(success = success, warnings = arrayListOf(), errors = arrayListOf(), obj = getFilesEntryByPath(path), result = result)
        } else {
            CommonOperationResult(success = false, warnings = arrayListOf(), errors = arrayListOf("Delete object not found"), result = CommonOpResultState.FAILED)
        }
    }

    fun getNewFileName(fileName: String): String {
        val prefix = UUID.randomUUID().toString()
        return fileName.replaceFirst(FILE_NAME_PATTERN, "$1_$prefix.$2")
    }

    fun getNewFileName(path: Path): String {
        return getNewFileName(path.fileName.toString())
    }

}