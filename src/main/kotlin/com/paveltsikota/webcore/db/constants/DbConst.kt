package com.paveltsikota.webcore.db.constants

//TODO: what gonna happen here?
object DbConst {
    val MAX_PAGE_SIZE = 25

    val SQL_GET_NOT_HASHED = "FROM FilesEntity p WHERE p.profile = :profile AND p.hashId IS NULL"
    val SQL_GET_NOT_HASHED_WITH_SIZE = "FROM FilesEntity p WHERE p.profile = :profile AND p.size = :size AND p.hashId IS NULL"
    val SQL_GET_NOT_GROUPED = "FROM FilesEntity p WHERE p.profile = :profile AND p.groupId IS NULL"
    val SQL_GET_NOT_GROUPED_WITH_SIZE = "FROM FilesEntity p WHERE p.profile = :profile AND p.size = :size AND p.groupId IS NULL"
    val SQL_GET_NOT_HASH_CALCULATED = "FROM FilesEntity p WHERE p.profile = :profile AND p.size = :size AND p.groupId IS NULL AND p.hashId IS NULL AND p.hash IS NULL and p.hashType IS NULL"
    val SQL_GET_NOT_HASH_CALCULATED_WITH_SIZE = "FROM FilesEntity p WHERE p.profile = :profile AND p.size = :size AND AND p.groupId IS NULL AND p.hashId IS NULL AND p.hash IS NULL and p.hashType IS NULL"

    val SQL_GET_GROUPS = "FROM GroupsEntity p ORDER BY p.id"
    val SQL_GET_GROUPS_BY_PROFILE = "FROM GroupsEntity p WHERE p.profile = :profile ORDER BY p.id"

    val SQL_GET_SOURCES = "FROM SourcesEntity p ORDER BY p.id"
    val SQL_GET_SOURCES_BY_IDS = "FROM SourcesEntity p WHERE p.id IN ORDER BY p.id"
    val SQL_GET_SOURCES_BY_PROFILE = "FROM SourcesEntity p WHERE p.profile = :profile ORDER BY p.id"
    val SQL_GET_BY_PROFILE_AND_PATH = "FROM SourcesEntity p WHERE p.profile = :profile AND p.path = :path ORDER BY p.id"

    val SQL_GET_HASHES = "FROM HashesEntity p ORDER BY p.id"
    val SQL_GET_HASHES_BY_PROFILE = "FROM HashesEntity p WHERE p.profile = :profile ORDER BY p.id"
    val SQL_GET_HASHES_BY_N = "FROM HashesEntity p WHERE json_array_length(p.duplicates) = :n ORDER BY p.id"
    val SQL_GET_HASHES_BY_PROFILE_AND_N = "FROM HashesEntity p WHERE p.profile = :profile AND json_array_length(p.duplicates) = :n ORDER BY p.id"

    val SQL_HASHES_ALREADY_EXIST = "FROM HashesEntity p WHERE p.profile = :profile AND p.size = :size AND p.hash = :hash AND p.hashType = : hashType LIMIT 1"
    val SQL_GET_HASHES_BY_PROFILE_AND_SIZE = "FROM HashesEntity p WHERE p.profile = :profile AND p.size = :size"

}