package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.ProfileEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ProfileDao(@PersistenceContext private val entityManager: EntityManager) {

    @Transactional
    fun save(profile: ProfileEntity) {
        entityManager.persist(profile)
    }

    @Transactional
    fun update(profile: ProfileEntity): ProfileEntity {
        return entityManager.merge(profile)
    }

    @Transactional
    fun findById(id: Long): ProfileEntity? {
        return entityManager.find(ProfileEntity::class.java, id)
    }

    @Transactional(readOnly = true)
    fun findByTitleAndProfile(title: String, profileId: Long): List<ProfileEntity> {
        val query = entityManager.createQuery(
            "FROM ProfileEntity p WHERE p.profile =: profile AND p.title = :title",
            ProfileEntity::class.java
        )
        query.setParameter("title", title)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByTitle(title: String): List<ProfileEntity> {
        val query = entityManager.createQuery(
            "FROM ProfileEntity p WHERE p.title = :title",
            ProfileEntity::class.java
        )
        query.setParameter("title", title)
        return query.resultList
    }

    @Transactional
    fun findAll(): List<ProfileEntity> {
        val cb = entityManager.criteriaBuilder
        val cq = cb.createQuery(ProfileEntity::class.java)
        val root = cq.from(ProfileEntity::class.java)
        cq.select(root)

        return entityManager.createQuery(cq).resultList
     }

    @Transactional
    fun removeById(id: Long): Boolean {
        val entity = entityManager.find(ProfileEntity::class.java, id)
        return if (entity != null) {
            entityManager.remove(entity)
            true
        } else {
            false
        }
    }
}