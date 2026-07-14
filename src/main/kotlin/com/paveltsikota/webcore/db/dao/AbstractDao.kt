package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.type.QueryOrderMap
import com.paveltsikota.webcore.db.type.QueryParamMap
import com.paveltsikota.webcore.db.type.toSqlQuery
import com.paveltsikota.webcore.utils.ValuesUtils.validatePageParams
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.transaction.annotation.Transactional
import kotlin.math.abs

@Transactional
abstract class AbstractDao<T: Any>(
    internal val entityClass: Class<T>
) {
    @PersistenceContext
    protected lateinit var entityManager: EntityManager

    @Transactional
    fun save(entity: T) {
        entityManager.persist(entity)
    }

    @Transactional
    fun update(entity: T): T {
        return entityManager.merge(entity)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): T? {
        return entityManager.find(entityClass, id)
    }

    @Transactional(readOnly = true)
    internal fun findByIds(ids: Collection<Long>): List<T>? {
        val query = entityManager.createQuery("FROM ${entityClass.name} p WHERE p.id IN :ids", entityClass)

        query.setParameter("ids", ids)
        return query.resultList
    }

    @Transactional
    fun removeById(id: Long): Boolean {
        val entity = entityManager.find(entityClass, id)

        if (entity != null) entityManager.remove(entity)

        return entity != null
    }

    @Transactional
    fun removeByIdAndProfile(id: Long, profileId: Long): Boolean {
        val query = entityManager
            .createQuery("FROM ${entityClass.name} p WHERE p.id = :id AND p.profile = :profile", entityClass)
            .setParameter("profile", profileId)

        val result = query.singleResultOrNull?.let { entityManager.remove(id) }

        return result != null
    }

    @Transactional(readOnly = true)
    internal fun getAll(page: Int, pageSize: Int, profileId: Long? = null): List<T>? {
        val offset = abs((page - 1) * pageSize)
        val sql = "SELECT f FROM ${entityClass.name} f ${if (profileId != null) { "WHERE f.profile = :profile" } else {}} ORDER BY f.id"

        val query = entityManager
            .createQuery(sql, entityClass)
            .setFirstResult(offset)
            .setMaxResults(pageSize)

        if (profileId != null) query.setParameter("profile", profileId)

        return query.resultList
    }

    @Transactional
    fun getBySql(sql: String, params: Map<String, Any>, page: Int? = null, pageSize: Int? = null): List<T>? {
        val pageValsOk = validatePageParams(page, pageSize)
        val offset = if (pageValsOk) { (page!! - 1) * pageSize!! } else { null }

        val query = entityManager.createQuery(sql, entityClass)

        params.forEach{ (p, v) -> query.setParameter(p, v) }

        if (pageValsOk) {
            query.firstResult = offset!!
            query.maxResults = pageSize!!
        }

        return query.resultList
    }

    @Transactional
    fun getByParams(params: QueryParamMap, orderBy: QueryOrderMap? = null, page: Int? = null, pageSize: Int? = null): List<T>? {
        val sql = "SELECT f FROM ${entityClass.name} f${params.toSqlQuery("f")}${orderBy?.toSqlQuery()}"
        return getBySql(sql, params, page, pageSize)
    }


}