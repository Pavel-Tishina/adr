package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.adapter.SourcesAdapter
import com.paveltsikota.webcore.db.constants.DbConst.SQL_GET_SOURCES
import com.paveltsikota.webcore.db.dao.SourcesDao
import com.paveltsikota.webcore.db.dto.SourcesDto
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.db.service.impl.SourcesServiceImpl
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.utils.FileUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.spy
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.nio.file.Path

class SourcesServiceImplTest {

    private lateinit var sourcesDao: SourcesDao
    private lateinit var service: SourcesServiceImpl

    private fun entity(id: Long, path: String, profile: Long, dirorder: Int) = SourcesEntity(id = id, path = "/path$id", profile = 1L, dirorder = 0)
    private fun dto(path: String, profile: Long = 1L, dirorder: Int = 0) = SourcesDto(path = path, profile = profile, dirorder = dirorder)

    @BeforeEach
    fun setup() {
        sourcesDao = mock()
        service = SourcesServiceImpl(sourcesDao)
    }



    @Test
    fun `getSource returns entity when found`() {
        val entity = SourcesEntity(id = 1L, path = "/test", profile = 42L, dirorder = 0)
        whenever(sourcesDao.findById(1L)).thenReturn(entity)

        val result = service.getSource(1L)

        assertTrue(result.success)
        assertEquals(EntityOperationResultType.ENTITY_FOUND, result.result)
        assertEquals(entity, result.obj)
    }

    @Test
    fun `getSource returns not found when dao returns null`() {
        whenever(sourcesDao.findById(1L)).thenReturn(null)

        val result = service.getSource(1L)

        assertFalse(result.success)
        assertEquals(EntityOperationResultType.ENTITY_NOT_FOUND, result.result)
        assertEquals("Source entity not found", result.error)
    }

    @Test
    fun `addSource returns already exist when source exists`() {
        val path = Path.of("/tmp/test")
        val source = SourcesEntity(path = FileUtils.toUnixPath(path), profile = 42L, dirorder = 0)

        val serviceSpy = spy(service)
        doReturn(true).`when`(serviceSpy).isAlreadyExist(source, true)

        val result = serviceSpy.addSource(path, 42L, 0, true)

        assertFalse(result.success)
        assertEquals(EntityOperationResultType.ENTITY_ALREADY_EXIST, result.result)
        assertEquals("Entity already exist", result.error)
    }

    @Test
    fun `addSource saves new entity when not exist`() {
        val path = Path.of("/tmp/test")
        val source = SourcesEntity(path = FileUtils.toUnixPath(path), profile = 42L, dirorder = 0)

        val serviceSpy = spy(service)
        doReturn(false).`when`(serviceSpy).isAlreadyExist(source, true)

        val result = serviceSpy.addSource(path, 42L, 0, true)

        assertTrue(result.success)
        assertEquals(EntityOperationResultType.ENTITY_ADD, result.result) // your code returns this type even on success
        assertEquals(source.path, (result.obj as SourcesEntity).path)
        //verify(sourcesDao).save(any(SourcesEntity::class.java))
    }

    @Test
    fun `removeSource returns removed when dao returns true`() {
        whenever(sourcesDao.removeById(10L)).thenReturn(true)

        val result = service.removeSource(10L)

        assertTrue(result.success)
        assertEquals(EntityOperationResultType.ENTITY_REMOVED, result.result)
        assertEquals(10L, result.obj)
    }

    @Test
    fun `removeSource returns not removed when dao returns false`() {
        whenever(sourcesDao.removeById(10L)).thenReturn(false)

        val result = service.removeSource(10L)

        assertFalse(result.success)
        assertEquals(EntityOperationResultType.ENTITY_NOT_REMOVED, result.result)
        assertEquals(10L, result.obj)
    }

    @Test
    fun `paged query returns entities`() {
        val expected = listOf(
            entity(id = 1, path = "/a", profile = 1L, dirorder = 1),
            entity(2, path = "/b", profile = 1L, dirorder = 2)
        )
        whenever(service.getBySql(SQL_GET_SOURCES, mapOf(), 1, 10)).thenReturn(expected)

        val result = service.getSources(page = 1, pageSize = 10, profileId = null)

        assertTrue(result.success)
        assertEquals(EntityOperationResultType.ENTITIES_FOUNDED, result.result)
        assertEquals(expected, result.obj)
    }

    @Test
    fun `paged query returns empty`() {
        whenever(service.getBySql(SQL_GET_SOURCES, mapOf(), 1, 10)).thenReturn(emptyList())

        val result = service.getSources(page = 1, pageSize = 10, profileId = null)

        assertFalse(result.success)
        assertEquals(EntityOperationResultType.ENTITIES_NOT_FOUNDED, result.result)
    }

    @Test
    fun `unpaged query loops until non-empty`() {
        val e = entity(3, path = "/c", profile = 1L, dirorder = 0)
        whenever(service.getBySql(SQL_GET_SOURCES, mapOf(), 1, 5)).thenReturn(listOf(e))

        val result = service.getSources(page = null, pageSize = 5, profileId = null)

        assertTrue(result.success)
        assertEquals(EntityOperationResultType.ENTITIES_FOUNDED, result.result)
        assertEquals(listOf(e), result.obj)
    }

    @Test
    fun `unpaged query empty`() {
        whenever(service.getBySql(SQL_GET_SOURCES, mapOf(), 1, 5)).thenReturn(emptyList())

        val result = service.getSources(page = null, pageSize = 5, profileId = null)

        assertFalse(result.success)
        assertEquals(EntityOperationResultType.ENTITIES_NOT_FOUNDED, result.result)
    }

//    @Test
//    fun `all sources added successfully`() {
//        val dtos = listOf(dto("/a"), dto("/b"))
//        whenever(service.addSource(any(), any(), any(), anyOrNull())).thenAnswer {
//            val p: String = it.getArgument(0)
//            val prof: Long = it.getArgument(1)
//            val dir: Int = it.getArgument(2)
//            EntityOperationResult(
//                success = true,
//                obj = listOf(
//                    entity(3, path = "/a", profile = 1, dirorder = 0),
//                    entity(4, path = "/b", profile = 1, dirorder = 1)
//                ),
//                result = EntityOperationResultType.ENTITY_ALREADY_EXIST)
//        }
//
//        val result = service.addSourcesDto(dtos, addOnce = true)
//
//        assertTrue(result.success)
//        assertEquals(EntityOperationResultType.ENTITY_ALREADY_EXIST, result.result)
//        assertEquals(2, (result.obj as Set<*>).size)
//    }
//
//    @Test
//    fun `all sources fail to add`() {
//        val dtos = listOf(dto("/a"), dto("/b"))
//        whenever(service.addSource(any(), any(), any(), anyOrNull())).thenReturn(
//            EntityOperationResult(success = false, error = "fail", result = EntityOperationResultType.ENTITY_NOT_ADD)
//        )
//
//        val result = service.addSourcesDto(dtos, addOnce = true)
//
//        assertFalse(result.success)
//        assertEquals(EntityOperationResultType.ENTITIES_NOT_ADDED, result.result)
//        assertTrue(result.error!!.contains("/a"))
//        assertTrue(result.error!!.contains("/b"))
//    }
//
//    @Test
//    fun `some sources added some failed`() {
//        val dtos = listOf(dto("/a"), dto("/b"))
//
//        whenever(service.addSource(any(), any(), any(), anyOrNull())).thenAnswer {
//            val path: Path = it.getArgument(0)
//            if (path.toString() == "/a") {
//                EntityOperationResult(success = true, obj = entity(id = 1, path = "/a", profile = 1, dirorder = 0),
//                    result = EntityOperationResultType.ENTITY_ALREADY_EXIST)
//            } else {
//                EntityOperationResult(success = false, error = "fail",
//                    result = EntityOperationResultType.ENTITY_NOT_ADD)
//            }
//        }
//
//        val result = service.addSourcesDto(dtos, addOnce = true)
//
//        assertTrue(result.success)
//        assertEquals(EntityOperationResultType.ENTITIES_ADDED_PARTLY, result.result)
//        assertTrue(result.error!!.contains("/b"))
//        assertEquals(1, (result.obj as Set<*>).size)
//    }
}
