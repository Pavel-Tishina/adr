package com.paveltsikota.webcore.utils

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import com.paveltsikota.webcore.service.operation.FileOperationResult
import com.paveltsikota.webcore.service.operation.enums.FileOpResultState
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.OsType
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.MOVE_TO_HASH_DIR_ERROR_FILE_NOT_EXIST
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.MOVE_TO_HASH_DIR_ERROR_SAME_PATH
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.MOVE_TO_HASH_DIR_ERROR_WRONG_STATUS
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.RESTORE_ERROR_ORIGINAL_FILE_NOT_EXIST
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.RESTORE_ERROR_RESTORED_FILE_ALREADY_EXIST
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.RESTORE_ERROR_SAME_PATH
import com.paveltsikota.webcore.utils.errors.FileOperationErrors.RESTORE_ERROR_WRONG_STATUS
import com.paveltsikota.webcore.utils.errors.FileOperationWarnings.WARN_FILE_RENAMED
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.FileTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileUtilsTest {
    val content = "There is some data!"
    val xxHash64 = XXHash64

    val NEW_FILE_NAME_PATTERN = Regex("""(.*?)([a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12})\..*""")

    @Test
    fun `unix path without stupid windows-slash`() {
        assertFalse { FileUtils.toUnixPath(FileUtils.getTempDir()).matches(Regex("\\\\")) }
    }

    @Test
    fun `success restore file`() {
        val tmpDir = FileUtils.getTempDir()
        val tmpFileOriginal = Path(tmpDir.toString(), "tmp.file")
        val tmpFileRestore = Path(tmpDir.toString(), "tmp_restore.file")
        val now = Date()

        Files.deleteIfExists(tmpFileOriginal)
        Files.deleteIfExists(tmpFileRestore)


        FileUtils.createFile(content, tmpFileOriginal, true)
        val hashOriginal = xxHash64.calculate(tmpFileOriginal)

        val restoreEntity = FilesEntity(
            size = content.length.toLong(),
            created = now.time - TimeUnit.DAYS.toMillis(10),
            modified = now.time - TimeUnit.DAYS.toMillis(5),
            path = FileUtils.toUnixPath(tmpFileRestore),
            fileName = tmpFileRestore.fileName.toString(),
            isUnique = false,
            hash = hashOriginal,
            hashType = xxHash64.getType(),
            state = FileState.DELETED,
            id = 0,
            profile = 0,
            hashPath = "",
            newFileName = "",
            groupId = 0,
            hashId = 0
        )

        val resultOfRestore = FileUtils.restoreFile(tmpFileOriginal, restoreEntity)
        val expectedResult = FileOperationResult(
            success = true,
            obj = restoreEntity.copy(state = FileState.ON_PLACE),
            result = FileOpResultState.FILE_RESTORED
        )

        assertTrue(sameFileOperationResult(expectedResult, resultOfRestore))
        assertTrue(resultOfRestore.success)
        assertFalse(resultOfRestore.hasDetails())
        assertEquals(FileOpResultState.FILE_RESTORED, resultOfRestore.result)
        assertEquals(FileState.ON_PLACE, resultOfRestore.obj.state)
        assertEquals(xxHash64.calculate(tmpFileRestore), resultOfRestore.obj.hash)
        assertEquals(File(tmpFileRestore.toString()).length(), resultOfRestore.obj.size)

        if (OsUtils.detectOS() == OsType.WINDOWS) {
            val creationDate = Files.getAttribute(tmpFileRestore, "creationTime") as FileTime

            assertTrue(creationDate.toInstant().atZone(ZoneId.systemDefault())
                .equals(Date(resultOfRestore.obj.created).toInstant().atZone(ZoneId.systemDefault())))
        }

        assertEquals(Files.getLastModifiedTime(tmpFileRestore).toMillis(), resultOfRestore.obj.modified)

        Files.deleteIfExists(tmpFileOriginal)
        Files.deleteIfExists(tmpFileRestore)
    }

    @Test
    fun `failed restore of file (diff cases)`() {
        val tmpDir = FileUtils.getTempDir()
        val tmpFileOriginal = Path(tmpDir.toString(), "tmp.file")
        val tmpFileRestoreSame = Path(tmpDir.toString(), "tmp.file")
        val tmpFileRestoreNotSame = Path(tmpDir.toString(), "tmp132.file")
        val now = Date()

        Files.deleteIfExists(tmpFileOriginal)
        Files.deleteIfExists(tmpFileRestoreNotSame)

        val restoreEntitySame = FilesEntity(
            size = content.length.toLong(),
            created = now.time - TimeUnit.DAYS.toMillis(10),
            modified = now.time - TimeUnit.DAYS.toMillis(5),
            path = FileUtils.toUnixPath(tmpFileRestoreSame),
            fileName = tmpFileRestoreSame.fileName.toString(),
            isUnique = false,
            hash = "",
            hashType = xxHash64.getType(),
            state = FileState.DELETED,
            id = 0,
            profile = 0,
            hashPath = "",
            newFileName = "",
            groupId = 0,
            hashId = 0
        )

        val restoreEntityStatusError = restoreEntitySame.copy(
            path = FileUtils.toUnixPath(tmpFileRestoreNotSame),
            fileName = tmpFileRestoreNotSame.fileName.toString(),
            state = FileState.MARK_MOVE
        )

        val restoreEntityRestoreExist = restoreEntityStatusError.copy(
            state = FileState.DELETED
        )

        val restoreEntityOriginalNotExist = restoreEntityStatusError.copy(
            state = FileState.DELETED
        )

        val expectResultSameError = FileOperationResult(
            errors = arrayListOf(RESTORE_ERROR_SAME_PATH), obj = restoreEntitySame, result = FileOpResultState.FILE_NOT_RESTORED)
        val expectResultStatusError = FileOperationResult(
            errors = arrayListOf(RESTORE_ERROR_WRONG_STATUS), obj = restoreEntityStatusError, result = FileOpResultState.FILE_NOT_RESTORED)
        val expectResultRestoreExist = FileOperationResult(
            errors = arrayListOf(RESTORE_ERROR_RESTORED_FILE_ALREADY_EXIST), obj = restoreEntityRestoreExist, result = FileOpResultState.FILE_NOT_RESTORED)
        val expectResultOriginalNotExist = FileOperationResult(
            errors = arrayListOf(RESTORE_ERROR_ORIGINAL_FILE_NOT_EXIST), obj = restoreEntityOriginalNotExist, result = FileOpResultState.FILE_NOT_RESTORED)

        FileUtils.createFile(content, tmpFileOriginal, true)
        val resultSameError = FileUtils.restoreFile(tmpFileOriginal, restoreEntitySame)
        val resultStatusError = FileUtils.restoreFile(tmpFileOriginal, restoreEntityStatusError)

        FileUtils.createFile(content, tmpFileRestoreNotSame, true)
        val resultRestoreExist = FileUtils.restoreFile(tmpFileOriginal, restoreEntityRestoreExist)

        Files.deleteIfExists(tmpFileOriginal)
        Files.deleteIfExists(tmpFileRestoreNotSame)
        val resultOriginalNotExist = FileUtils.restoreFile(tmpFileOriginal, restoreEntityOriginalNotExist)

        Files.deleteIfExists(tmpFileOriginal)
        Files.deleteIfExists(tmpFileRestoreNotSame)

        assertTrue(sameFileOperationResult(expectResultSameError, resultSameError))
        assertTrue(sameFileOperationResult(expectResultStatusError, resultStatusError))
        assertTrue(sameFileOperationResult(expectResultRestoreExist, resultRestoreExist))
        assertTrue(sameFileOperationResult(expectResultOriginalNotExist, resultOriginalNotExist))
    }

    @Test
    fun `new file name test`() {
        val name1 = Path("/some/where/my/file1.txt")
        val name2 = Path("/some/where/my/file2.doc.txt")
        val name3 = Path("/some/where/my/file3_gg._gg.txt")
        val name4 = Path("/some/where/my/___ff_file3_gg_.._gg._txt")
        assertTrue(FileUtils.getNewFileName(name1).matches(NEW_FILE_NAME_PATTERN))
        assertTrue(FileUtils.getNewFileName(name2).matches(NEW_FILE_NAME_PATTERN))
        assertTrue(FileUtils.getNewFileName(name3).matches(NEW_FILE_NAME_PATTERN))
        assertTrue(FileUtils.getNewFileName(name4).matches(NEW_FILE_NAME_PATTERN))
    }


    @Test
    fun `success - file copy to hash dir operation`() {
        val tmpDir = FileUtils.getTempDir()
        val rootHashDir = Path(tmpDir.toString(), "xxhash")
        val sourceFile = Path(tmpDir.toString(), "tmp.file")
        val hash = xxHash64.calculate(ByteArrayInputStream(content.toByteArray()))
        val destFile = Path(rootHashDir.toString(), hash, "tmp.file")

        val now = Date()

        Files.deleteIfExists(sourceFile)
        Files.deleteIfExists(destFile)
        FileUtils.deleteDirOrFile(rootHashDir)

        val sourceEntity = FilesEntity(
            size = content.length.toLong(),
            created = now.time - TimeUnit.DAYS.toMillis(10),
            modified = now.time - TimeUnit.DAYS.toMillis(5),
            path = FileUtils.toUnixPath(sourceFile),
            fileName = sourceFile.fileName.toString(),
            isUnique = false,
            hash = hash,
            hashType = xxHash64.getType(),
            state = FileState.MARK_MOVE,
            id = 0,
            profile = 0,
            hashPath = "",
            newFileName = "",
            groupId = 0,
            hashId = 0
        )

        FileUtils.createFile(content, sourceFile, true)

        val expectedResult = FileOperationResult(
            success = true, obj = sourceEntity.copy(state = FileState.MOVED), result = FileOpResultState.FILE_MOVED)
        val resultOperation = FileUtils.copyFileToHashDir(sourceEntity, rootHashDir)

        val resultOperationWithRename = FileUtils.copyFileToHashDir(sourceEntity.copy(state = FileState.MARK_MOVE), rootHashDir)
        val expectedResultNewName = FileOperationResult(
            success = true, warnings = arrayListOf(resultOperationWithRename.getWarnings()), obj = sourceEntity.copy(state = FileState.MOVED), result = FileOpResultState.FILE_MOVED)

        assertTrue(sameFileOperationResult(expectedResult, resultOperation))
        assertTrue(sameFileOperationResult(expectedResultNewName, resultOperationWithRename))
        with(resultOperationWithRename) {
            assertTrue(hasDetails())
            assertTrue(getWarnings().startsWith(WARN_FILE_RENAMED))
            assertTrue(obj.newFileName.matches(NEW_FILE_NAME_PATTERN))
        }

        Files.deleteIfExists(sourceFile)
        Files.deleteIfExists(destFile)
        with(resultOperationWithRename.obj) {
            val movedFileWithNewName = Path(rootHashDir.toString(), hash, newFileName)
            Files.deleteIfExists(movedFileWithNewName)
            Files.deleteIfExists(movedFileWithNewName.parent)
            Files.deleteIfExists(movedFileWithNewName.parent.parent)
        }
    }

    @Test
    fun `failed - file copy to hash dir operation(diff cases)`() {
        val tmpDir = FileUtils.getTempDir()
        val rootHashDir = Path(tmpDir.toString(), "xxhash")
        val sourceFile = Path(tmpDir.toString(), "tmp.file")
        val hash = xxHash64.calculate(ByteArrayInputStream(content.toByteArray()))
        val destFileSame = Path(rootHashDir.toString(), hash, "tmp.file")

        Files.deleteIfExists(sourceFile)
        Files.deleteIfExists(destFileSame)
        FileUtils.deleteDirOrFile(rootHashDir)

        val wrongStatusEntity = FilesEntityUtils.getFilesEntryByPath(path = sourceFile)
        val samePathEntity = wrongStatusEntity.copy(
            path = FileUtils.toUnixPath(destFileSame), hash = hash, state = FileState.MARK_MOVE)

        val expectedSourceNotExistResult = FileOperationResult(
            success = false,
            errors = arrayListOf(MOVE_TO_HASH_DIR_ERROR_FILE_NOT_EXIST),
            obj = wrongStatusEntity.copy(state = FileState.MARK_MOVE),
            result = FileOpResultState.FILE_NOT_MOVED
        )
        val sourceNotExistResult = FileUtils.copyFileToHashDir(file = wrongStatusEntity.copy(state = FileState.MARK_MOVE), rootHashDir)

        FileUtils.createFile(content, sourceFile, true)
        FileUtils.createFile(content, destFileSame, true)
        val expectedSamePathResult = FileOperationResult(
            success = false,
            errors = arrayListOf(MOVE_TO_HASH_DIR_ERROR_SAME_PATH),
            obj = samePathEntity,
            result = FileOpResultState.FILE_NOT_MOVED
        )
        val samePathResult = FileUtils.copyFileToHashDir(file = samePathEntity, rootHashDir)

        val expectedWrongStateResult = FileOperationResult(
            success = false,
            errors = arrayListOf(MOVE_TO_HASH_DIR_ERROR_WRONG_STATUS),
            obj = wrongStatusEntity.copy(state = FileState.DELETED),
            result = FileOpResultState.FILE_NOT_MOVED
        )
        val wrongStateResult = FileUtils.copyFileToHashDir(file = wrongStatusEntity.copy(state = FileState.DELETED), rootHashDir)

        assertTrue(sameFileOperationResult(expectedSourceNotExistResult, sourceNotExistResult))
        assertTrue(sameFileOperationResult(expectedWrongStateResult, wrongStateResult))
        assertTrue(sameFileOperationResult(expectedSamePathResult, samePathResult))

        Files.deleteIfExists(sourceFile)
        Files.deleteIfExists(destFileSame)
        FileUtils.deleteDirOrFile(rootHashDir)
    }

    private fun sameFileOperationResult(r1: FileOperationResult, r2: FileOperationResult): Boolean {
        return r1.success == r2.success
                && r1.result == r2.result
                && r1.getWarnings() == r2.getWarnings()
                && r1.getError() == r2.getError()
                && formalEqFileEntity(r1.obj, r2.obj)
    }

    private fun formalEqFileEntity(e1: FilesEntity, e2: FilesEntity): Boolean {
        return e1.id == e2.id && e1.profile == e2.profile && e1.state == e2.state
    }


}