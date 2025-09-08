package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.ProfileEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ProfileDao: CommonDao<ProfileEntity>(ProfileEntity::class.java) {

    @Transactional(readOnly = true)
    fun findByTitleAndProfile(title: String, profileId: Long): List<ProfileEntity> {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile =: profile AND p.title = :title", entityClass)

        query.setParameter("title", title)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByTitle(title: String): List<ProfileEntity> {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.title = :title", entityClass)

        query.setParameter("title", title)
        return query.resultList
    }

}