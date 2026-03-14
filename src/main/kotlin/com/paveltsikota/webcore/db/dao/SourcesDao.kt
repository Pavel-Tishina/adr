package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.SourcesEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SourcesDao: AbstractDao<SourcesEntity>(SourcesEntity::class.java) {

    @Transactional(readOnly = true)
    fun findByProfileAndId(id: Long, profileId: Long): SourcesEntity? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.id = :id AND p.profile = :profile", entityClass)

        query.setParameter("id", id)
        query.setParameter("profile", profileId)
        return query.singleResultOrNull
    }

    @Transactional(readOnly = true)
    fun getByIds(ids: List<Long>): List<SourcesEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.id IN :ids ORDER BY p.dirorder", entityClass)

        query.setParameter("ids", ids)
        return query.resultList
    }

    @Transactional
    fun removeByProfileId(profileId: Long): Int {
        val query = entityManager.createQuery(
            "DELETE FROM ${entityClass.name} p WHERE p.profile = :profile")

        query.setParameter("profile", profileId)
        return query.executeUpdate()
    }

}