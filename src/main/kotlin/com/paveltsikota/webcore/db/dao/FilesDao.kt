package com.paveltsikota.webcore.db.service.impl.dao

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.utils.enums.HashType
import org.hibernate.SessionFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class FilesDao(private val sessionFactory: SessionFactory) {

    @Transactional
    fun save(file: FilesEntity) {
        sessionFactory.currentSession.use { session ->
            session.persist(file)
        }
    }

    @Transactional
    fun update(file: FilesEntity) {
        sessionFactory.currentSession.use { session ->
            session.merge(file) // если объект detached, merge обновит запись в БД
        }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): FilesEntity? {
        return sessionFactory.currentSession.use { session ->
            session.find(FilesEntity::class.java, id)
        }
    }

    @Transactional(readOnly = true)
    fun findBySize(size: Long, profileId: Long): List<FilesEntity>? {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM files p WHERE p.size = :size AND p.profile = :profile",
            FilesEntity::class.java
        )
        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByHash(hash: String, hashType: HashType, profileId: Long): List<FilesEntity>? {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM files p WHERE p.profile = :profile AND hashType = :hashType AND hash = :hash",
            FilesEntity::class.java
        )
        query.setParameter("hash", hash)
        query.setParameter("hashType", hashType)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByHashId(hashId: Long, profileId: Long): List<FilesEntity>? {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM files p WHERE p.profile = :profile AND hashId = :hashId",
            FilesEntity::class.java
        )
        query.setParameter("hashId", hashId)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByGroupId(groupId: Long, profileId: Long): List<FilesEntity>? {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM files p WHERE p.profile = :profile AND groupId = :groupId",
            FilesEntity::class.java
        )
        query.setParameter("groupId", groupId)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    fun removeById(id: Long) {
        sessionFactory.currentSession.use { session ->
            val entity = session.find(FilesEntity::class.java, id)
            if (entity != null) {
                session.remove(entity)
            }
        }
    }

}