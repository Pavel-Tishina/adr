package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.utils.enums.JobStatus
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class JobsDao: AbstractDao<JobsEntity>(JobsEntity::class.java) {

    @Transactional(readOnly = true)
    fun findByProfileId(profileId: Long): List<JobsEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile ORDER BY p.id", entityClass)

        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByUuidAndProfileId(uuid: String, profileId: Long): List<JobsEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile AND p.uuid = :uuid ORDER BY p.id", entityClass)

        query.setParameter("profile", profileId)
        query.setParameter("uuid", uuid)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByStatusAndProfileId(status: JobStatus, profileId: Long): List<JobsEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile AND p.status = :status ORDER BY p.id", entityClass)

        query.setParameter("profile", profileId)
        query.setParameter("status", status)
        return query.resultList
    }

}
