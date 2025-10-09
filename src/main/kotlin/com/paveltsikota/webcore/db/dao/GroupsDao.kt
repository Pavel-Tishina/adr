package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.GroupsEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class GroupsDao: AbstractDao<GroupsEntity>(GroupsEntity::class.java) {

    @Transactional(readOnly = true)
    fun findBySizeAndProfileId(size: Long, profileId: Long): List<GroupsEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.size = :size AND p.profile = :profile", entityClass)

        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

}