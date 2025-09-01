package com.paveltsikota.webcore.db.service.impl.dao

import com.paveltsikota.webcore.db.entity.SourcesEntity
import org.hibernate.SessionFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SourcesDao(private val sessionFactory: SessionFactory) {

    @Transactional
    fun save(source: SourcesEntity) {
        sessionFactory.currentSession.use { session ->
            session.persist(source) // заменяет старый save()
        }
    }

    @Transactional
    fun update(source: SourcesEntity) {
        sessionFactory.currentSession.use { session ->
            session.merge(source) // если объект detached, merge обновит запись в БД
        }
    }

    @Transactional
    fun findById(id: Long): SourcesEntity? {
        return sessionFactory.currentSession.use { session ->
            session.find(SourcesEntity::class.java, id) // заменяет get()
        }
    }

    @Transactional(readOnly = true)
    fun findByProfileId(profileId: Long): List<SourcesEntity> {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM sources p WHERE p.profile = :profile",
            SourcesEntity::class.java
        )
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    fun removeById(id: Long) {
        sessionFactory.currentSession.use { session ->
            val entity = session.find(SourcesEntity::class.java, id)
            if (entity != null) {
                session.remove(entity)
            }
        }
    }

    @Transactional
    fun removeByProfileId(profileId: Long) {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            "DELETE FROM sources p WHERE p.profile = :profile",
            SourcesEntity::class.java
        )
        query.setParameter("profile", profileId)
        query.executeUpdate()
    }

}