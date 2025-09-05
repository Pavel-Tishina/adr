package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.JobsEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class JobsDao(@PersistenceContext private val entityManager: EntityManager) {

    @Transactional
    fun save(job: JobsEntity) {
        entityManager.persist(job)
    }

    @Transactional
    fun update(job: JobsEntity): JobsEntity {
        return entityManager.merge(job) // если объект detached, merge обновит запись в БД
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): JobsEntity? {
        return entityManager.find(JobsEntity::class.java, id)
    }

    @Transactional(readOnly = true)
    fun findByIdAndProfileId(id: Long, profileId: Long): JobsEntity? {
        val query = entityManager.createQuery(
            "FROM JobsEntity p WHERE p.id = :id AND p.profile = :profile",
            JobsEntity::class.java
        )
        query.setParameter("id", id)
        query.setParameter("profile", profileId)
        return query.singleResultOrNull
    }

    @Transactional(readOnly = true)
    fun findByProfileId(size: Long, profileId: Long): List<JobsEntity>? {
        val query = entityManager.createQuery(
            "FROM JobsEntity p WHERE p.profile = :profile ORDER BY p.priority",
            JobsEntity::class.java
        )
        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    fun removeById(id: Long): Boolean {
        val entity = entityManager.find(JobsEntity::class.java, id)
        return if (entity != null) {
            entityManager.remove(entity)
            true
        } else {
            false
        }
    }

}