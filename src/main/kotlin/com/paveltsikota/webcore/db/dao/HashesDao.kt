package com.paveltsikota.webcore.db.service.impl.dao

import com.paveltsikota.webcore.db.entity.HashesEntity
import org.hibernate.SessionFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class HashesDao(private val sessionFactory: SessionFactory) {

    @Transactional
    fun save(hash: HashesEntity) {
        sessionFactory.currentSession.use { session ->
            session.persist(hash)
        }
    }

    @Transactional
    fun update(hash: HashesEntity) {
        sessionFactory.currentSession.use { session ->
            session.merge(hash) // если объект detached, merge обновит запись в БД
        }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): HashesEntity? {
        return sessionFactory.currentSession.use { session ->
            session.find(HashesEntity::class.java, id)
        }
    }

    @Transactional(readOnly = true)
    fun findByHashAndProfileId(hash: String, profileId: Long): HashesEntity? {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM hashes p WHERE p.profile = :profile AND p.hash = :hash",
            HashesEntity::class.java
        )
        query.setParameter("hash", hash)
        query.setParameter("profile", profileId)
        return query.singleResultOrNull
    }

    @Transactional(readOnly = true)
    fun findBySizeAndProfileId(size: Long, profileId: Long): List<HashesEntity>? {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM hashes p WHERE p.size = :size AND p.profile = :profile",
            HashesEntity::class.java
        )
        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    fun removeById(id: Long) {
        sessionFactory.currentSession.use { session ->
            val entity = session.find(HashesEntity::class.java, id)
            if (entity != null) {
                session.remove(entity)
            }
        }
    }

}