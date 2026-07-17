package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.db.dto.HistoryElementDto
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class EntitiesTest {

    // ===================== FilesEntity =====================

    @Test
    fun `FilesEntity - default instances are equal via same()`() {
        val e1 = FilesEntity()
        val e2 = FilesEntity()
        assertTrue(e1.same(e2))
        assertTrue(e2.same(e1))
    }

    @Test
    fun `FilesEntity - equals uses id + same()`() {
        val e1 = FilesEntity(id = 1L)
        val e2 = FilesEntity(id = 1L)
        assertEquals(e1, e2)
    }

    @Test
    fun `FilesEntity - different ids means not equals`() {
        val e1 = FilesEntity(id = 1L)
        val e2 = FilesEntity(id = 2L)
        assertNotEquals(e1, e2)
    }

    @Test
    fun `FilesEntity - same ignores id, only compares fields`() {
        val e1 = FilesEntity(id = 1L, path = "/a")
        val e2 = FilesEntity(id = 99L, path = "/a")
        assertTrue(e1.same(e2))
    }

    @Test
    fun `FilesEntity - different path makes same() false`() {
        val e1 = FilesEntity(path = "/a")
        val e2 = FilesEntity(path = "/b")
        assertFalse(e1.same(e2))
    }

    @Test
    fun `FilesEntity - different state makes same() false`() {
        val e1 = FilesEntity(state = FileState.ON_PLACE)
        val e2 = FilesEntity(state = FileState.MARK_DEL)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `FilesEntity - different hold makes same() false`() {
        val e1 = FilesEntity(hold = true)
        val e2 = FilesEntity(hold = false)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `FilesEntity - different hashType makes same() false`() {
        val e1 = FilesEntity(hashType = HashType.MD5)
        val e2 = FilesEntity(hashType = HashType.SHA256)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `FilesEntity - same with null optional fields`() {
        val e1 = FilesEntity(hashPath = null, newFileName = null, isUnique = null, groupId = null, hashId = null)
        val e2 = FilesEntity(hashPath = null, newFileName = null, isUnique = null, groupId = null, hashId = null)
        assertTrue(e1.same(e2))
    }

    @Test
    fun `FilesEntity - different null vs non-null optional field makes same() false`() {
        val e1 = FilesEntity(groupId = null)
        val e2 = FilesEntity(groupId = 10L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `FilesEntity - not equals with non-FilesEntity`() {
        val e1 = FilesEntity()
        assertFalse(e1.equals("not an entity"))
    }

    // ===================== GroupsEntity =====================

    @Test
    fun `GroupsEntity - default instances are same`() {
        val e1 = GroupsEntity()
        val e2 = GroupsEntity()
        assertTrue(e1.same(e2))
    }

    @Test
    fun `GroupsEntity - different fileIds makes same() false`() {
        val e1 = GroupsEntity(fileIds = hashSetOf(1L, 2L))
        val e2 = GroupsEntity(fileIds = hashSetOf(1L, 3L))
        assertFalse(e1.same(e2))
    }

    @Test
    fun `GroupsEntity - different size makes same() false`() {
        val e1 = GroupsEntity(size = 100L)
        val e2 = GroupsEntity(size = 200L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `GroupsEntity - same with matching jobId`() {
        val e1 = GroupsEntity(jobId = 5L, fileIds = hashSetOf(1L))
        val e2 = GroupsEntity(jobId = 5L, fileIds = hashSetOf(1L))
        assertTrue(e1.same(e2))
    }

    @Test
    fun `GroupsEntity - different jobId makes same() false`() {
        val e1 = GroupsEntity(jobId = 1L)
        val e2 = GroupsEntity(jobId = 2L)
        assertFalse(e1.same(e2))
    }

    // ===================== HashesEntity =====================

    @Test
    fun `HashesEntity - default instances are same`() {
        val e1 = HashesEntity()
        val e2 = HashesEntity()
        assertTrue(e1.same(e2))
    }

    @Test
    fun `HashesEntity - different hash makes same() false`() {
        val e1 = HashesEntity(hash = "abc123")
        val e2 = HashesEntity(hash = "def456")
        assertFalse(e1.same(e2))
    }

    @Test
    fun `HashesEntity - different hashType makes same() false`() {
        val e1 = HashesEntity(hashType = HashType.MD5)
        val e2 = HashesEntity(hashType = HashType.SHA256)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `HashesEntity - different main makes same() false`() {
        val e1 = HashesEntity(main = 1L)
        val e2 = HashesEntity(main = 2L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `HashesEntity - different duplicates makes same() false`() {
        val e1 = HashesEntity(duplicates = mutableSetOf(1L, 2L))
        val e2 = HashesEntity(duplicates = mutableSetOf(1L, 3L))
        assertFalse(e1.same(e2))
    }

    @Test
    fun `HashesEntity - same with matching duplicates`() {
        val e1 = HashesEntity(duplicates = mutableSetOf(1L, 2L))
        val e2 = HashesEntity(duplicates = mutableSetOf(1L, 2L))
        assertTrue(e1.same(e2))
    }

    // ===================== ProfileEntity =====================

    @Test
    fun `ProfileEntity - default instances are same`() {
        val e1 = ProfileEntity()
        val e2 = ProfileEntity()
        assertTrue(e1.same(e2))
    }

    @Test
    fun `ProfileEntity - different title makes same() false`() {
        val e1 = ProfileEntity(title = "A")
        val e2 = ProfileEntity(title = "B")
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ProfileEntity - different description makes same() false`() {
        val e1 = ProfileEntity(description = "desc1")
        val e2 = ProfileEntity(description = "desc2")
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ProfileEntity - different cfg makes same() false`() {
        val e1 = ProfileEntity(cfg = mapOf("key" to "value1"))
        val e2 = ProfileEntity(cfg = mapOf("key" to "value2"))
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ProfileEntity - not equals with non-ProfileEntity`() {
        val e1 = ProfileEntity()
        assertFalse(e1.equals("not an entity"))
    }

    // ===================== SourcesEntity =====================

    @Test
    fun `SourcesEntity - default instances are same`() {
        val e1 = SourcesEntity()
        val e2 = SourcesEntity()
        assertTrue(e1.same(e2))
    }

    @Test
    fun `SourcesEntity - different path makes same() false`() {
        val e1 = SourcesEntity(path = "/a")
        val e2 = SourcesEntity(path = "/b")
        assertFalse(e1.same(e2))
    }

    @Test
    fun `SourcesEntity - different dirorder makes same() false`() {
        val e1 = SourcesEntity(dirorder = 0)
        val e2 = SourcesEntity(dirorder = 1)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `SourcesEntity - different profile makes same() false`() {
        val e1 = SourcesEntity(profile = 1L)
        val e2 = SourcesEntity(profile = 2L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `SourcesEntity - same with same fields different id`() {
        val e1 = SourcesEntity(id = 1L, path = "/x", profile = 5L, dirorder = 3)
        val e2 = SourcesEntity(id = 99L, path = "/x", profile = 5L, dirorder = 3)
        assertTrue(e1.same(e2))
    }

    // ===================== JobsEntity =====================

    @Test
    fun `JobsEntity - default instances are same`() {
        val e1 = JobsEntity()
        val e2 = JobsEntity()
        assertTrue(e1.same(e2))
    }

    @Test
    fun `JobsEntity - different status makes same() false`() {
        val e1 = JobsEntity(status = JobStatus.CREATED)
        val e2 = JobsEntity(status = JobStatus.RUNNING)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsEntity - different uuid makes same() false`() {
        val e1 = JobsEntity(uuid = "abc")
        val e2 = JobsEntity(uuid = "def")
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsEntity - different disabled makes same() false`() {
        val e1 = JobsEntity(disabled = true)
        val e2 = JobsEntity(disabled = false)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsEntity - different start makes same() false`() {
        val e1 = JobsEntity(start = 100L)
        val e2 = JobsEntity(start = 200L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsEntity - different finish makes same() false`() {
        val e1 = JobsEntity(finish = 100L)
        val e2 = JobsEntity(finish = 200L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsEntity - same with matching taskList and history`() {
        val history = mutableListOf(HistoryElementDto(start = 1L, finish = 2L))
        val e1 = JobsEntity(taskList = mutableListOf(1L, 2L), history = history)
        val e2 = JobsEntity(taskList = mutableListOf(1L, 2L), history = history)
        assertTrue(e1.same(e2))
    }

    @Test
    fun `JobsEntity - different taskList makes same() false`() {
        val e1 = JobsEntity(taskList = mutableListOf(1L))
        val e2 = JobsEntity(taskList = mutableListOf(2L))
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsEntity - not equals with non-JobsEntity`() {
        val e1 = JobsEntity()
        assertFalse(e1.equals("not an entity"))
    }

    // ===================== JobsTaskEntity =====================

    @Test
    fun `JobsTaskEntity - default instances are same`() {
        val e1 = JobsTaskEntity()
        val e2 = JobsTaskEntity()
        assertTrue(e1.same(e2))
    }

    @Test
    fun `JobsTaskEntity - different priority makes same() false`() {
        val e1 = JobsTaskEntity(priority = 0)
        val e2 = JobsTaskEntity(priority = 1)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsTaskEntity - different type makes same() false`() {
        val e1 = JobsTaskEntity(type = JobsType.DRAFT)
        val e2 = JobsTaskEntity(type = JobsType.FILE_SCAN)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsTaskEntity - different status makes same() false`() {
        val e1 = JobsTaskEntity(status = JobStatus.CREATED)
        val e2 = JobsTaskEntity(status = JobStatus.COMPLETED)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsTaskEntity - different lastObjectId makes same() false`() {
        val e1 = JobsTaskEntity(lastObjectId = 1L)
        val e2 = JobsTaskEntity(lastObjectId = 2L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsTaskEntity - different jobId makes same() false`() {
        val e1 = JobsTaskEntity(jobId = 1L)
        val e2 = JobsTaskEntity(jobId = 2L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsTaskEntity - different objects makes same() false`() {
        val e1 = JobsTaskEntity(objects = mutableListOf(1L, 2L))
        val e2 = JobsTaskEntity(objects = mutableListOf(1L, 3L))
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsTaskEntity - same with matching history`() {
        val history = mutableListOf(HistoryElementDto(start = 10L, finish = 20L))
        val e1 = JobsTaskEntity(history = history, objects = mutableListOf(1L))
        val e2 = JobsTaskEntity(history = history, objects = mutableListOf(1L))
        assertTrue(e1.same(e2))
    }

    @Test
    fun `JobsTaskEntity - different history makes same() false`() {
        val e1 = JobsTaskEntity(history = mutableListOf(HistoryElementDto(start = 1L)))
        val e2 = JobsTaskEntity(history = mutableListOf(HistoryElementDto(start = 2L)))
        assertFalse(e1.same(e2))
    }

    @Test
    fun `JobsTaskEntity - not equals with non-JobsTaskEntity`() {
        val e1 = JobsTaskEntity()
        assertFalse(e1.equals("not an entity"))
    }

    // ===================== ConfigEntity =====================

    @Test
    fun `ConfigEntity - default instances are same`() {
        val e1 = ConfigEntity()
        val e2 = ConfigEntity()
        assertTrue(e1.same(e2))
    }

    @Test
    fun `ConfigEntity - different hashDir makes same() false`() {
        val e1 = ConfigEntity(hashDir = "/dir1")
        val e2 = ConfigEntity(hashDir = "/dir2")
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ConfigEntity - different hashType makes same() false`() {
        val e1 = ConfigEntity(hashType = HashType.MD5)
        val e2 = ConfigEntity(hashType = HashType.SHA256)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ConfigEntity - different bufferSize makes same() false`() {
        val e1 = ConfigEntity(bufferSize = 1024L)
        val e2 = ConfigEntity(bufferSize = 2048L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ConfigEntity - different progressN makes same() false`() {
        val e1 = ConfigEntity(progressN = 100)
        val e2 = ConfigEntity(progressN = 200)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ConfigEntity - different progressSize makes same() false`() {
        val e1 = ConfigEntity(progressSize = 1000L)
        val e2 = ConfigEntity(progressSize = 2000L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ConfigEntity - different progressShow makes same() false`() {
        val e1 = ConfigEntity(progressShow = true)
        val e2 = ConfigEntity(progressShow = false)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ConfigEntity - different flyHashCalculate makes same() false`() {
        val e1 = ConfigEntity(flyHashCalculate = true)
        val e2 = ConfigEntity(flyHashCalculate = false)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ConfigEntity - different profile makes same() false`() {
        val e1 = ConfigEntity(profile = 1L)
        val e2 = ConfigEntity(profile = 2L)
        assertFalse(e1.same(e2))
    }

    @Test
    fun `ConfigEntity - not equals with non-ConfigEntity`() {
        val e1 = ConfigEntity()
        assertFalse(e1.equals("not an entity"))
    }
}
