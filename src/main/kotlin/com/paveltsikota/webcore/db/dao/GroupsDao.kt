package com.paveltsikota.webcore.db.service.impl.dao

import com.paveltsikota.webcore.db.entity.GroupsEntity
import org.hibernate.SessionFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class GroupsDao(private val sessionFactory: SessionFactory) {

    @Transactional
    fun save(group: GroupsEntity) {
        sessionFactory.currentSession.use { session ->
            session.persist(group)
        }
    }

    @Transactional
    fun update(group: GroupsEntity) {
        sessionFactory.currentSession.use { session ->
            session.merge(group) // если объект detached, merge обновит запись в БД
        }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): GroupsEntity? {
        return sessionFactory.currentSession.use { session ->
            session.find(GroupsEntity::class.java, id)
        }
    }

    @Transactional(readOnly = true)
    fun findByIdAndProfileId(id: Long, profileId: Long): GroupsEntity? {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM groups p WHERE p.id = :id AND p.profile = :profile",
            GroupsEntity::class.java
        )
        query.setParameter("id", id)
        query.setParameter("profile", profileId)
        return query.singleResultOrNull
    }

    @Transactional(readOnly = true)
    fun findBySizeAndProfileId(size: Long, profileId: Long): List<GroupsEntity>? {
        val session = sessionFactory.currentSession
        val query = session.createQuery(
            //"FROM ProfileEntity p WHERE p.title = :title",
            "FROM groups p WHERE p.size = :size AND p.profile = :profile",
            GroupsEntity::class.java
        )
        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    fun removeById(id: Long) {
        sessionFactory.currentSession.use { session ->
            val entity = session.find(GroupsEntity::class.java, id)
            if (entity != null) {
                session.remove(entity)
            }
        }
    }

}