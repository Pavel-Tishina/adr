package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.SourcesEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SourcesDao: CommonDao<SourcesEntity>(SourcesEntity::class.java) {

    @Transactional(readOnly = true)
    fun findByProfileId(profileId: Long): List<SourcesEntity> {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile", entityClass)

        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    fun removeByProfileId(profileId: Long) {
        val query = entityManager.createQuery(
            "DELETE FROM ${entityClass.name} p WHERE p.profile = :profile", entityClass)

        query.setParameter("profile", profileId)
        query.executeUpdate()
    }

}