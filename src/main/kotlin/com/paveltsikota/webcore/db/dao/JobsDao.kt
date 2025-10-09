package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.JobsEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class JobsDao: AbstractDao<JobsEntity>(JobsEntity::class.java) {

    @Transactional(readOnly = true)
    fun findByIdAndProfileId(profileId: Long): JobsEntity? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile", entityClass)

        query.setParameter("profile", profileId)
        return query.singleResultOrNull
    }

    @Transactional(readOnly = true)
    fun findByProfileIdAndPriority(profileId: Long, priority: Int): JobsEntity? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile AND p.priority = :priority", entityClass)

        query.setParameter("priority", priority)
        query.setParameter("profile", profileId)
        return query.singleResultOrNull
    }

    @Transactional(readOnly = true)
    fun findByProfileId(profileId: Long): List<JobsEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile ORDER BY p.priority", entityClass)

        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findLastPriority(profileId: Long): Int {
        val query = entityManager.createQuery(
            "SELECT MAX(e.priority) FROM ${entityClass.name} p WHERE p.profile = :profile", Int::class.java)

        query.setParameter("profile", profileId)
        return query.singleResult?: 0
    }

}