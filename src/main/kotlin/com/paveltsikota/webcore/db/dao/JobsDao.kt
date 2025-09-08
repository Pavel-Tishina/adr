package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.JobsEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class JobsDao: CommonDao<JobsEntity>(JobsEntity::class.java) {

    @Transactional(readOnly = true)
    fun findByIdAndProfileId(id: Long, profileId: Long): JobsEntity? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.id = :id AND p.profile = :profile", entityClass)

        query.setParameter("id", id)
        query.setParameter("profile", profileId)
        return query.singleResultOrNull
    }

    @Transactional(readOnly = true)
    fun findByProfileId(size: Long, profileId: Long): List<JobsEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile ORDER BY p.priority", entityClass)

        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

}