package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.utils.FileUtils
import com.paveltsikota.webcore.utils.FilesEntityUtils.hashNotCalculated
import com.paveltsikota.webcore.utils.enums.HashType
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path

@Repository
class FilesDao(@PersistenceContext private val entityManager: EntityManager) {

    @Transactional
    fun save(file: FilesEntity) {
        entityManager.persist(file)
    }

    @Transactional
    fun update(file: FilesEntity): FilesEntity {
        return entityManager.merge(file)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): FilesEntity? {
        return entityManager.find(FilesEntity::class.java, id)
    }

    @Transactional(readOnly = true)
    fun findBySize(size: Long, profileId: Long): List<FilesEntity>? {
        val query = entityManager.createQuery(
            "FROM FilesEntity p WHERE p.size = :size AND p.profile = :profile",
            FilesEntity::class.java
        )
        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun checkAlreadyExistEntity(path: Path, size: Long, hash: String? = null, hashType: HashType? = null, profileId: Long): Boolean {
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
    fun findByHash(hash: String, hashType: HashType, profileId: Long): List<FilesEntity>? {
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
    fun findByHashId(hashId: Long, profileId: Long): List<FilesEntity>? {
        val query = entityManager.createQuery(
            "FROM FilesEntity p WHERE p.profile = :profile AND hashId = :hashId",
            FilesEntity::class.java
        )
        query.setParameter("hashId", hashId)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByGroupId(groupId: Long, profileId: Long): List<FilesEntity>? {
        val query = entityManager.createQuery(
            "FROM FilesEntity p WHERE p.profile = :profile AND groupId = :groupId",
            FilesEntity::class.java
        )
        query.setParameter("groupId", groupId)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    fun removeById(id: Long): Boolean {
        var isOk = false

        val entity = entityManager.find(FilesEntity::class.java, id)
        if (entity != null) {
            entityManager.remove(entity)
            isOk = true
        }

        return isOk
    }

}