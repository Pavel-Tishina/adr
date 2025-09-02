package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.HashesEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class HashesDao(@PersistenceContext private val entityManager: EntityManager) {

    @Transactional
    fun save(hash: HashesEntity) {
        entityManager.persist(hash)
    }

    @Transactional
    fun update(hash: HashesEntity): HashesEntity {
        return entityManager.merge(hash) // если объект detached, merge обновит запись в БД
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): HashesEntity? {
        return entityManager.find(HashesEntity::class.java, id)
    }

    @Transactional(readOnly = true)
    fun findByHashAndProfileId(hash: String, profileId: Long): HashesEntity? {
        val query = entityManager.createQuery(
            "FROM HashesEntity p WHERE p.profile = :profile AND p.hash = :hash",
            HashesEntity::class.java
        )
        query.setParameter("hash", hash)
        query.setParameter("profile", profileId)
        return query.singleResultOrNull
    }

    @Transactional(readOnly = true)
    fun findBySizeAndProfileId(size: Long, profileId: Long): List<HashesEntity>? {
        val query = entityManager.createQuery(
            "FROM HashesEntity p WHERE p.size = :size AND p.profile = :profile",
            HashesEntity::class.java
        )
        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    fun removeById(id: Long): Boolean {
        val entity = entityManager.find(HashesEntity::class.java, id)
        return if (entity != null) {
            entityManager.remove(entity)
            true
        } else {
            false
        }
    }

}