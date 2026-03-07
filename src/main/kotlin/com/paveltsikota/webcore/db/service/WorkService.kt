package com.paveltsikota.webcore.db.service

interface WorkService {
    fun addFilesFromDirectory(profileId: Long, sourcesId: List<Long>)
    fun makeGroups(profileId: Long)
    fun calculateHashes(profileId: Long)
}