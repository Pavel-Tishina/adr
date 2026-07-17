package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.entity.GroupsEntity
import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.db.entity.JobsTaskEntity
import com.paveltsikota.webcore.db.entity.ProfileEntity
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.utils.enums.HashType
import com.paveltsikota.webcore.utils.enums.JobStatus
import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import jakarta.persistence.TypedQuery
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.test.util.ReflectionTestUtils

class DaosTest {

    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun setup() {
        entityManager = mock()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> mockTypedListQuery(result: List<T> = emptyList()): TypedQuery<T> {
        val query = mock<TypedQuery<T>>()
        doReturn(query).`when`(entityManager).createQuery(any<String>(), org.mockito.ArgumentMatchers.any<Class<T>>())
        doReturn(result).`when`(query).resultList
        doReturn(query).`when`(query).setParameter(any<String>(), any())
        doReturn(query).`when`(query).setFirstResult(anyInt())
        doReturn(query).`when`(query).setMaxResults(anyInt())
        return query
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> mockSingleResultQuery(result: T? = null): TypedQuery<T> {
        val query = mock<TypedQuery<T>>()
        doReturn(query).`when`(entityManager).createQuery(any<String>(), org.mockito.ArgumentMatchers.any<Class<T>>())
        doReturn(result).`when`(query).singleResultOrNull
        doReturn(query).`when`(query).setParameter(any<String>(), any())
        return query
    }

    private fun injectEm(dao: Any) {
        ReflectionTestUtils.setField(dao, "entityManager", entityManager)
    }

    // ===================== FilesDao =====================

    @Nested
    inner class FilesDaoTest {
        private lateinit var dao: FilesDao

        @BeforeEach
        fun setup() {
            dao = FilesDao()
            injectEm(dao)
        }

        @Test
        fun `save calls entityManager persist`() {
            val entity = FilesEntity(path = "/test/file.txt", profile = 1L)
            dao.save(entity)
            verify(entityManager).persist(entity)
        }

        @Test
        fun `update calls entityManager merge`() {
            val entity = FilesEntity(id = 1L, path = "/test/file.txt")
            doReturn(entity).`when`(entityManager).merge(entity)
            val result = dao.update(entity)
            verify(entityManager).merge(entity)
            assertEquals(entity, result)
        }

        @Test
        fun `findById returns entity when found`() {
            val entity = FilesEntity(id = 1L, path = "/a")
            doReturn(entity).`when`(entityManager).find(FilesEntity::class.java, 1L)
            assertEquals(entity, dao.findById(1L))
        }

        @Test
        fun `findById returns null when not found`() {
            doReturn(null).`when`(entityManager).find(FilesEntity::class.java, 999L)
            assertNull(dao.findById(999L))
        }

        @Test
        fun `removeById returns true when entity exists`() {
            val entity = FilesEntity(id = 1L)
            doReturn(entity).`when`(entityManager).find(FilesEntity::class.java, 1L)
            assertTrue(dao.removeById(1L))
            verify(entityManager).remove(entity)
        }

        @Test
        fun `removeById returns false when not found`() {
            doReturn(null).`when`(entityManager).find(FilesEntity::class.java, 999L)
            assertFalse(dao.removeById(999L))
        }

        @Test
        fun `findBySize delegates to correct JPQL`() {
            mockTypedListQuery(listOf(FilesEntity(id = 1L)))
            dao.findBySize(page = 1, pageSize = 10, size = 1024L, profileId = 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(FilesEntity::class.java))
            assertTrue(captor.firstValue.contains("p.size = :size"))
            assertTrue(captor.firstValue.contains("p.profile = :profile"))
        }

        @Test
        fun `findByHash delegates to correct JPQL`() {
            mockTypedListQuery(listOf(FilesEntity(id = 1L)))
            dao.findByHash("abc123", HashType.MD5, 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(FilesEntity::class.java))
            assertTrue(captor.firstValue.contains("hashType = :hashType"))
            assertTrue(captor.firstValue.contains("hash = :hash"))
        }

        @Test
        fun `findByHashId delegates to correct JPQL`() {
            mockTypedListQuery(listOf(FilesEntity(id = 1L)))
            dao.findByHashId(10L, 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(FilesEntity::class.java))
            assertTrue(captor.firstValue.contains("hashId = :hashId"))
        }

        @Test
        fun `findByGroupId delegates to correct JPQL`() {
            mockTypedListQuery(listOf(FilesEntity(id = 1L)))
            dao.findByGroupId(5L, 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(FilesEntity::class.java))
            assertTrue(captor.firstValue.contains("groupId = :groupId"))
        }
    }

    // ===================== GroupsDao =====================

    @Nested
    inner class GroupsDaoTest {
        private lateinit var dao: GroupsDao

        @BeforeEach
        fun setup() {
            dao = GroupsDao()
            injectEm(dao)
        }

        @Test
        fun `save calls entityManager persist`() {
            val entity = GroupsEntity(profile = 1L, size = 100L)
            dao.save(entity)
            verify(entityManager).persist(entity)
        }

        @Test
        fun `findById returns entity`() {
            val entity = GroupsEntity(id = 1L, profile = 1L, size = 100L)
            doReturn(entity).`when`(entityManager).find(GroupsEntity::class.java, 1L)
            assertEquals(entity, dao.findById(1L))
        }

        @Test
        fun `findBySizeAndProfileId delegates to correct JPQL`() {
            mockTypedListQuery(listOf(GroupsEntity(id = 1L)))
            dao.findBySizeAndProfileId(100L, 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(GroupsEntity::class.java))
            assertTrue(captor.firstValue.contains("p.size = :size"))
            assertTrue(captor.firstValue.contains("p.profile = :profile"))
        }
    }

    // ===================== HashesDao =====================

    @Nested
    inner class HashesDaoTest {
        private lateinit var dao: HashesDao

        @BeforeEach
        fun setup() {
            dao = HashesDao()
            injectEm(dao)
        }

        @Test
        fun `findByHashAndProfileId uses parameterized hashType`() {
            val entity = HashesEntity(id = 1L, hash = "abc", hashType = HashType.MD5, profile = 1L)
            mockSingleResultQuery(entity)
            val result = dao.findByHashAndProfileId("abc", HashType.MD5, 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(HashesEntity::class.java))
            assertTrue(captor.firstValue.contains("p.hashType = :hashType"), "JPQL must use :hashType parameter")
            assertEquals(entity, result)
        }

        @Test
        fun `findByHashAndProfileId returns null when not found`() {
            mockSingleResultQuery<HashesEntity>(null)
            assertNull(dao.findByHashAndProfileId("nonexistent", HashType.MD5, 1L))
        }

        @Test
        fun `findBySizeAndProfileId delegates to correct JPQL`() {
            mockTypedListQuery(listOf(HashesEntity(id = 1L)))
            dao.findBySizeAndProfileId(1024L, 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(HashesEntity::class.java))
            assertTrue(captor.firstValue.contains("p.size = :size"))
            assertTrue(captor.firstValue.contains("p.profile = :profile"))
        }
    }

    // ===================== JobsDao =====================

    @Nested
    inner class JobsDaoTest {
        private lateinit var dao: JobsDao

        @BeforeEach
        fun setup() {
            dao = JobsDao()
            injectEm(dao)
        }

        @Test
        fun `findByProfileId delegates to correct JPQL`() {
            mockTypedListQuery(listOf(JobsEntity(id = 1L)))
            dao.findByProfileId(1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(JobsEntity::class.java))
            assertTrue(captor.firstValue.contains("p.profile = :profile"))
            assertTrue(captor.firstValue.contains("ORDER BY p.id"))
        }

        @Test
        fun `findByUuidAndProfileId delegates to correct JPQL`() {
            mockTypedListQuery(listOf(JobsEntity(id = 1L)))
            dao.findByUuidAndProfileId("test-uuid", 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(JobsEntity::class.java))
            assertTrue(captor.firstValue.contains("p.uuid = :uuid"))
        }

        @Test
        fun `findByStatusAndProfileId delegates to correct JPQL`() {
            mockTypedListQuery(listOf(JobsEntity(id = 1L)))
            dao.findByStatusAndProfileId(JobStatus.RUNNING, 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(JobsEntity::class.java))
            assertTrue(captor.firstValue.contains("p.status = :status"))
        }
    }

    // ===================== JobsTaskDao =====================

    @Nested
    inner class JobsTaskDaoTest {
        private lateinit var dao: JobsTaskDao

        @BeforeEach
        fun setup() {
            dao = JobsTaskDao()
            injectEm(dao)
        }

        @Test
        fun `findByProfileId uses p priority not p piority`() {
            mockTypedListQuery(listOf(JobsTaskEntity(id = 1L)))
            dao.findByProfileId(1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(JobsTaskEntity::class.java))
            assertTrue(captor.firstValue.contains("p.priority"), "Must use 'p.priority', not 'p.piority'")
            assertFalse(captor.firstValue.contains("piority"), "Typo 'piority' must not appear")
        }

        @Test
        fun `findByProfileIdAndPriority delegates to correct JPQL`() {
            val entity = JobsTaskEntity(id = 1L, priority = 5, profile = 1L)
            mockSingleResultQuery(entity)
            val result = dao.findByProfileIdAndPriority(1L, 5)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(JobsTaskEntity::class.java))
            assertTrue(captor.firstValue.contains("p.priority = :priority"))
            assertEquals(entity, result)
        }

        @Test
        fun `findByJobId delegates to correct JPQL`() {
            mockTypedListQuery(listOf(JobsTaskEntity(id = 1L)))
            dao.findByJobId(10L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(JobsTaskEntity::class.java))
            assertTrue(captor.firstValue.contains("p.jobId = :jobId"))
        }

        @Test
        fun `findByStatusAndProfileId delegates to correct JPQL`() {
            mockTypedListQuery(listOf(JobsTaskEntity(id = 1L)))
            dao.findByStatusAndProfileId(JobStatus.CREATED, 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(JobsTaskEntity::class.java))
            assertTrue(captor.firstValue.contains("p.status = :status"))
        }
    }

    // ===================== ProfileDao =====================

    @Nested
    inner class ProfileDaoTest {
        private lateinit var dao: ProfileDao

        @BeforeEach
        fun setup() {
            dao = ProfileDao()
            injectEm(dao)
        }

        @Test
        fun `findByTitle uses parameterized query`() {
            val entity = ProfileEntity(id = 1L, title = "Default")
            mockSingleResultQuery(entity)
            val result = dao.findByTitle("Default")
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(ProfileEntity::class.java))
            assertTrue(captor.firstValue.contains("p.title = :title"))
            assertEquals(entity, result)
        }

        @Test
        fun `findByTitleAndProfile uses colon before profile param`() {
            mockTypedListQuery(listOf(ProfileEntity(id = 1L)))
            dao.findByTitleAndProfile("Default", 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(ProfileEntity::class.java))
            assertTrue(captor.firstValue.contains("p.profile = :profile"), "Must use ':profile' with colon")
            assertFalse(captor.firstValue.contains("=: profile"), "Must not have space after colon in '=:'")
        }
    }

    // ===================== SourcesDao =====================

    @Nested
    inner class SourcesDaoTest {
        private lateinit var dao: SourcesDao

        @BeforeEach
        fun setup() {
            dao = SourcesDao()
            injectEm(dao)
        }

        @Test
        fun `findByProfileAndId delegates to correct JPQL`() {
            val entity = SourcesEntity(id = 1L, path = "/a", profile = 1L, dirorder = 0)
            mockSingleResultQuery(entity)
            val result = dao.findByProfileAndId(1L, 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(SourcesEntity::class.java))
            assertTrue(captor.firstValue.contains("p.id = :id"))
            assertTrue(captor.firstValue.contains("p.profile = :profile"))
            assertEquals(entity, result)
        }

        @Test
        fun `getByIds delegates to correct JPQL`() {
            mockTypedListQuery(listOf(SourcesEntity(id = 1L)))
            dao.getByIds(listOf(1L, 2L))
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(SourcesEntity::class.java))
            assertTrue(captor.firstValue.contains("p.id IN :ids"))
            assertTrue(captor.firstValue.contains("ORDER BY p.dirorder"))
        }

        @Test
        fun `removeByProfileId executes delete`() {
            val query = mock<jakarta.persistence.Query>()
            doReturn(query).`when`(entityManager).createQuery(any<String>())
            doReturn(query).`when`(query).setParameter(any<String>(), any())
            doReturn(3).`when`(query).executeUpdate()

            val result = dao.removeByProfileId(1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture())
            assertTrue(captor.firstValue.contains("DELETE FROM"))
            assertTrue(captor.firstValue.contains("p.profile = :profile"))
            assertEquals(3, result)
        }
    }

    // ===================== AbstractDao generic =====================

    @Nested
    inner class AbstractDaoGenericTest {
        private lateinit var dao: FilesDao

        @BeforeEach
        fun setup() {
            dao = FilesDao()
            injectEm(dao)
        }

        @Test
        fun `getBySql delegates to EntityManager with params`() {
            mockTypedListQuery(listOf(FilesEntity(id = 1L)))
            dao.getBySql("FROM FilesEntity p WHERE p.profile = :profile", mapOf("profile" to 1L), page = 1, pageSize = 10)
            val sqlCaptor = argumentCaptor<String>()
            verify(entityManager).createQuery(sqlCaptor.capture(), eq(FilesEntity::class.java))
            assertEquals("FROM FilesEntity p WHERE p.profile = :profile", sqlCaptor.firstValue)
        }

        @Test
        fun `getAll delegates to correct paginated query`() {
            mockTypedListQuery(listOf(FilesEntity(id = 1L)))
            dao.getAll(page = 1, pageSize = 10, profileId = 1L)
            val sqlCaptor = argumentCaptor<String>()
            verify(entityManager).createQuery(sqlCaptor.capture(), eq(FilesEntity::class.java))
            assertTrue(sqlCaptor.firstValue.contains("WHERE f.profile = :profile"))
            assertTrue(sqlCaptor.firstValue.contains("ORDER BY f.id"))
        }

        @Test
        fun `removeByIdAndProfile queries by id and profile`() {
            val entity = FilesEntity(id = 1L, profile = 1L)
            mockSingleResultQuery<FilesEntity>(entity)
            dao.removeByIdAndProfile(1L, 1L)
            val captor = argumentCaptor<String>()
            verify(entityManager).createQuery(captor.capture(), eq(FilesEntity::class.java))
            assertTrue(captor.firstValue.contains("p.id = :id"))
            assertTrue(captor.firstValue.contains("p.profile = :profile"))
        }
    }
}


