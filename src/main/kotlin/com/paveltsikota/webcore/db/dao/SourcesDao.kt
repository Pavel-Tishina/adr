package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.utils.ValuesUtils.validatePageParams
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SourcesDao(@PersistenceContext private val entityManager: EntityManager) {

    @Transactional
    fun save(source: SourcesEntity) {
        entityManager.persist(source)
    }

    @Transactional
    fun update(source: SourcesEntity): SourcesEntity {
        return entityManager.merge(source)
    }

    @Transactional
    fun findById(id: Long): SourcesEntity? {
        return entityManager.find(SourcesEntity::class.java, id)
    }

    @Transactional(readOnly = true)
    fun findByProfileId(profileId: Long): List<SourcesEntity> {
        val query = entityManager.createQuery(
            "FROM SourcesEntity p WHERE p.profile = :profile",
            SourcesEntity::class.java
        )
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    fun removeById(id: Long): Boolean {
        val entity = entityManager.find(SourcesEntity::class.java, id)
        return if (entity != null) {
            entityManager.remove(entity)
            true
        } else {
            false
        }
    }

    @Transactional
    fun removeByProfileId(profileId: Long) {
        val query = entityManager.createQuery(
            "DELETE FROM SourcesEntity p WHERE p.profile = :profile",
            SourcesEntity::class.java
        )
        query.setParameter("profile", profileId)
        query.executeUpdate()
    }

    @Transactional
    internal fun getBySql(sql: String, params: Map<String, Any>, page: Int? = null, pageSize: Int? = null): List<SourcesEntity>? {
        val pageValsOk = validatePageParams(page, pageSize)
        val offset = if (pageValsOk) { (page!! - 1) * pageSize!! } else { null }

        val query = entityManager.createQuery(sql, SourcesEntity::class.java)

        params.forEach{ query.setParameter(it.key, it.value) }

        if (pageValsOk) {
            query.setFirstResult(offset!!)
            query.setMaxResults(pageSize!!)
        }

        return query.resultList
    }

}