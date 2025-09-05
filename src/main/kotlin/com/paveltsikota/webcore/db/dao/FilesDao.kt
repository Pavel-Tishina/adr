package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.utils.FileUtils
import com.paveltsikota.webcore.utils.FilesEntityUtils.hashNotCalculated
import com.paveltsikota.webcore.utils.ValuesUtils.validatePageParams
import com.paveltsikota.webcore.utils.enums.HashType
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path

@Repository
class FilesDao(@PersistenceContext private val entityManager: EntityManager) {

    @Transactional
    internal fun save(file: FilesEntity) {
        entityManager.persist(file)
    }

    @Transactional
    internal fun update(file: FilesEntity): FilesEntity {
        return entityManager.merge(file)
    }

    @Transactional(readOnly = true)
    internal fun findById(id: Long): FilesEntity? {
        return entityManager.find(FilesEntity::class.java, id)
    }

    @Transactional(readOnly = true)
    internal fun findByIds(ids: Collection<Long>): List<FilesEntity>? {
        val query = entityManager.createQuery(
            "FROM FilesEntity p WHERE p.id IN :ids",
            FilesEntity::class.java
        )

        query.setParameter("ids", ids)
        return query.resultList
    }

    @Transactional(readOnly = true)
    internal fun getFiles(page: Int, pageSize: Int, profileId: Long? = null): List<FilesEntity>? {
        val offset = (page - 1) * pageSize
        val sql = if (profileId == null) {
            "SELECT f FROM FilesEntity f ORDER BY f.id"
        } else {
            "SELECT f FROM FilesEntity f WHERE f.profile = :profile ORDER BY f.id"
        }
        val query = entityManager
            .createQuery(sql, FilesEntity::class.java)
            .setFirstResult(offset)
            .setMaxResults(pageSize)

        if (profileId != null) {
            query.setParameter("profile", profileId)
        }

        return query.resultList
    }

    @Transactional(readOnly = true)
    internal fun findBySize(page: Int? = null, pageSize: Int? = null, size: Long, profileId: Long): List<FilesEntity>? {
        val pageValsOk = validatePageParams(page, pageSize)
        val offset = if (pageValsOk) { (page!! - 1) * pageSize!! } else { null }

        val query = entityManager.createQuery(
            "FROM FilesEntity p WHERE p.size = :size AND p.profile = :profile",
            FilesEntity::class.java
        )

        if (pageValsOk) {
            query.setFirstResult(offset!!)
            query.setMaxResults(pageSize!!)
        }

        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    internal fun checkAlreadyExistEntity(path: Path, size: Long, hash: String? = null, hashType: HashType? = null, profileId: Long): Boolean {
        val hashNotCalc = hashNotCalculated(hash, hashType)
        val sql = if (hashNotCalc) {
            "FROM FilesEntity p WHERE p.size = :size AND p.profile = :profile AND p.path = :path"
        } else {
            "FROM FilesEntity p WHERE p.size = :size AND p.profile = :profile AND p.path = :path AND p.hash = :hash AND p.hashType = :hashType"
        }
        val query = entityManager.createQuery(sql, FilesEntity::class.java)

        if (!hashNotCalc) {
            query.setParameter("hash", hash)
            query.setParameter("hashType", hashType)
        }

        query.setParameter("path", FileUtils.toUnixPath(path))
        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        query.setMaxResults(1)

        return query.resultList?.isNotEmpty()?: false
    }

    @Transactional(readOnly = true)
    internal fun findByHash(hash: String, hashType: HashType, profileId: Long): List<FilesEntity>? {
        val query = entityManager.createQuery(
            "FROM FilesEntity p WHERE p.profile = :profile AND hashType = :hashType AND hash = :hash",
            FilesEntity::class.java
        )
        query.setParameter("hash", hash)
        query.setParameter("hashType", hashType)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    internal fun findByHashId(hashId: Long, profileId: Long): List<FilesEntity>? {
        val query = entityManager.createQuery(
            "FROM FilesEntity p WHERE p.profile = :profile AND hashId = :hashId",
            FilesEntity::class.java
        )
        query.setParameter("hashId", hashId)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    internal fun findByGroupId(groupId: Long, profileId: Long): List<FilesEntity>? {
        val query = entityManager.createQuery(
            "FROM FilesEntity p WHERE p.profile = :profile AND groupId = :groupId",
            FilesEntity::class.java
        )
        query.setParameter("groupId", groupId)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    internal fun removeById(id: Long): Boolean {
        var isOk = false

        val entity = entityManager.find(FilesEntity::class.java, id)
        if (entity != null) {
            entityManager.remove(entity)
            isOk = true
        }

        return isOk
    }

    @Transactional
    internal fun getBySql(sql: String, params: Map<String, Any>, page: Int? = null, pageSize: Int? = null): List<FilesEntity>? {
        val pageValsOk = validatePageParams(page, pageSize)
        val offset = if (pageValsOk) { (page!! - 1) * pageSize!! } else { null }

        val query = entityManager.createQuery(sql, FilesEntity::class.java)

        params.forEach{ query.setParameter(it.key, it.value) }
        
        if (pageValsOk) {
            query.setFirstResult(offset!!)
            query.setMaxResults(pageSize!!)
        }

        return query.resultList
    }

}