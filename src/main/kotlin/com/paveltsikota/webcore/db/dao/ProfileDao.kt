package com.paveltsikota.webcore.db.service.impl.dao

import com.paveltsikota.webcore.db.entity.ProfileEntity
import org.hibernate.SessionFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ProfileDao(private val sessionFactory: SessionFactory) {

    @Transactional
    fun save(profile: ProfileEntity) {
        sessionFactory.currentSession.use { session ->
            session.persist(profile) // заменяет старый save()
        }
    }

    @Transactional
    fun update(profile: ProfileEntity) {
        sessionFactory.currentSession.use { session ->
            session.merge(profile) // если объект detached, merge обновит запись в БД
        }
    }

    @Transactional
    fun findById(id: Long): ProfileEntity? {
        return sessionFactory.currentSession.use { session ->
            session.find(ProfileEntity::class.java, id) // заменяет get()
        }
    }

    @Transactional(readOnly = true)
    fun findByTitleAndProfile(title: String, profileId: Long): List<ProfileEntity> {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM profile p WHERE p.profile =: profile AND p.title = :title",
            ProfileEntity::class.java
        )
        query.setParameter("title", title)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    fun findByTitle(title: String): List<ProfileEntity> {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM profile p WHERE p.title = :title",
            ProfileEntity::class.java
        )
        query.setParameter("title", title)
        return query.resultList
    }

    @Transactional
    fun findAll(): List<ProfileEntity> {
        return sessionFactory.currentSession.use { session ->
            val cb = session.criteriaBuilder
            val cq = cb.createQuery(ProfileEntity::class.java)
            val root = cq.from(ProfileEntity::class.java)
            cq.select(root)
            session.createQuery(cq).resultList
        }
    }

    @Transactional
    fun removeById(id: Long) {
        sessionFactory.currentSession.use { session ->
            val entity = session.find(ProfileEntity::class.java, id)
            if (entity != null) {
                session.remove(entity)
            }
        }
    }
}