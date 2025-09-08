package com.paveltsikota.webcore.db.dao

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.utils.FileUtils
import com.paveltsikota.webcore.utils.FilesEntityUtils.hashNotCalculated
import com.paveltsikota.webcore.utils.ValuesUtils.validatePageParams
import com.paveltsikota.webcore.utils.enums.HashType
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path

@Repository
class FilesDao: CommonDao<FilesEntity>(FilesEntity::class.java) {

    @Transactional(readOnly = true)
    internal fun findBySize(page: Int? = null, pageSize: Int? = null, size: Long, profileId: Long): List<FilesEntity>? {
        val pageValsOk = validatePageParams(page, pageSize)
        val offset = if (pageValsOk) { (page!! - 1) * pageSize!! } else { null }

        val query = entityManager.createQuery("FROM ${entityClass.name} p WHERE p.size = :size AND p.profile = :profile", entityClass)

        if (pageValsOk) {
            query.firstResult = offset!!
            query.maxResults = pageSize!!
        }

        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    internal fun checkAlreadyExistEntity(path: Path, size: Long, hash: String? = null, hashType: HashType? = null, profileId: Long): Boolean {
        val hashNotCalc = hashNotCalculated(hash, hashType)
        val sql = if (hashNotCalc) {
            "FROM ${entityClass.name} p WHERE p.size = :size AND p.profile = :profile AND p.path = :path"
        } else {
            "FROM ${entityClass.name} p WHERE p.size = :size AND p.profile = :profile AND p.path = :path AND p.hash = :hash AND p.hashType = :hashType"
        }
        val query = entityManager.createQuery(sql, entityClass)

        if (!hashNotCalc) {
            query.setParameter("hash", hash)
            query.setParameter("hashType", hashType)
        }

        query.setParameter("path", FileUtils.toUnixPath(path))
        query.setParameter("size", size)
        query.setParameter("profile", profileId)
        query.maxResults = 1

        return query.resultList?.isNotEmpty() == true
    }

    @Transactional(readOnly = true)
    internal fun findByHash(hash: String, hashType: HashType, profileId: Long): List<FilesEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile AND hashType = :hashType AND hash = :hash", entityClass)

        query.setParameter("hash", hash)
        query.setParameter("hashType", hashType)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    internal fun findByHashId(hashId: Long, profileId: Long): List<FilesEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile AND hashId = :hashId", entityClass)

        query.setParameter("hashId", hashId)
        query.setParameter("profile", profileId)
        return query.resultList
    }

    @Transactional(readOnly = true)
    internal fun findByGroupId(groupId: Long, profileId: Long): List<FilesEntity>? {
        val query = entityManager.createQuery(
            "FROM ${entityClass.name} p WHERE p.profile = :profile AND groupId = :groupId", entityClass)

        query.setParameter("groupId", groupId)
        query.setParameter("profile", profileId)
        return query.resultList
    }

}