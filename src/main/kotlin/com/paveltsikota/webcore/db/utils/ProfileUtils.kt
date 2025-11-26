package com.paveltsikota.webcore.db.utils

import com.paveltsikota.webcore.db.entity.ProfileEntity
import com.paveltsikota.webcore.db.utils.ConfigUtils.getDefaultConfigMap
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE_DESCRIPTION
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE_TITLE

object ProfileUtils {
    fun isDefaultProfile(e: ProfileEntity?): Boolean = e != null && e.id == 1L

    fun makeDefaultProfileEntity(): ProfileEntity {
        return ProfileEntity(
            id = DEFAULT_PROFILE,
            title = DEFAULT_PROFILE_TITLE,
            description = DEFAULT_PROFILE_DESCRIPTION,
            cfg = getDefaultConfigMap()
        )
    }
}