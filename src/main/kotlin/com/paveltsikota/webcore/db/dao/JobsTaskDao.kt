package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.JobsTaskEntity
import com.paveltsikota.webcore.utils.enums.JobStatus
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class JobsTaskDao: AbstractDao<JobsTaskEntity>(JobsTaskEntity::class.java) {

    @Transactional(readOnly = true)
    fun findByProfileId(profileId: Long): List<JobsTaskEntity> {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile ORDER BY p.piority", entityClass)

        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByProfileIdAndPriority(profileId: Long, priority: Int): JobsTaskEntity? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile AND p.priority = :priority", entityClass)

        query.setParameter("priority", priority)
        query.setParameter("profile", profileId)
        return query.singleResultOrNull
    }

    @Transactional(readOnly = true)
    fun findLastPriority(profileId: Long): Int {
        val query = entityManager.createQuery(
            "SELECT MAX(e.priority) FROM ${entityClass.name} p WHERE p.profile = :profile", Int::class.java)

        query.setParameter("profile", profileId)
        return query.singleResult?: 0
    }

    @Transactional(readOnly = true)
    fun findByJobId(jobId: Long): List<JobsTaskEntity> {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.jobId = :jobId ORDER BY p.priority", entityClass)

        query.setParameter("jobId", jobId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByStatusAndProfileId(status: JobStatus, profileId: Long): List<JobsTaskEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile AND p.status = :status ORDER BY p.priority", entityClass)

        query.setParameter("profile", profileId)
        query.setParameter("status", status)
        return query.resultList
    }

}