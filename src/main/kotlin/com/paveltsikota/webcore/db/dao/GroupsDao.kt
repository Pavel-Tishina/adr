package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.GroupsEntity
import com.paveltsikota.webcore.utils.ValuesUtils.validatePageParams
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class GroupsDao(@PersistenceContext private val entityManager: EntityManager) {

    @Transactional
    fun save(group: GroupsEntity) {
        entityManager.persist(group)
    }

    @Transactional
    fun update(group: GroupsEntity): GroupsEntity {
        return entityManager.merge(group) // если объект detached, merge обновит запись в БД
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): GroupsEntity? {
        return entityManager.find(GroupsEntity::class.java, id)
    }

    @Transactional(readOnly = true)
    fun findBySizeAndProfileId(size: Long, profileId: Long): List<GroupsEntity>? {
        val query = entityManager.createQuery(
            "FROM GroupsEntity p WHERE p.size = :size AND p.profile = :profile",
            GroupsEntity::class.java
        )
        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional
    fun removeById(id: Long): Boolean {
        val entity = entityManager.find(GroupsEntity::class.java, id)
        return if (entity != null) {
            entityManager.remove(entity)
            true
        } else {
            false
        }
    }

    @Transactional
    internal fun getBySql(sql: String, params: Map<String, Any>, page: Int? = null, pageSize: Int? = null): List<GroupsEntity>? {
        val pageValsOk = validatePageParams(page, pageSize)
        val offset = if (pageValsOk) { (page!! - 1) * pageSize!! } else { null }

        val query = entityManager.createQuery(sql, GroupsEntity::class.java)

        if (params.isNotEmpty()) {
            params.forEach{ query.setParameter(it.key, it.value) }
        }

        if (pageValsOk) {
            query.setFirstResult(offset!!)
            query.setMaxResults(pageSize!!)
        }

        return query.resultList
    }

}