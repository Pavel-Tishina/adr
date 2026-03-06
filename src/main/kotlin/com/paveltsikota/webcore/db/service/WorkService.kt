package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.types.DataTypeAlias.*

interface WorkService {
    fun addFilesFromDirectory(profileId: ProfileType, sourcesId: List<IdType>)
    fun makeGroups(profileId: ProfileType)
    fun calculateHashes(profileId: ProfileType)
}