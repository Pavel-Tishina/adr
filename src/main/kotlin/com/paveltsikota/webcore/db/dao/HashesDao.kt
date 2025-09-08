package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.HashesEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class HashesDao: CommonDao<HashesEntity>(HashesEntity::class.java) {

    @Transactional(readOnly = true)
    fun findByHashAndProfileId(hash: String, profileId: Long): HashesEntity? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile AND p.hash = :hash", entityClass)

        query.setParameter("hash", hash)
        query.setParameter("profile", profileId)
        return query.singleResultOrNull
    }

    @Transactional(readOnly = true)
    fun findBySizeAndProfileId(size: Long, profileId: Long): List<HashesEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.size = :size AND p.profile = :profile", entityClass)

        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

}