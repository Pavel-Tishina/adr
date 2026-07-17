package com.paveltsikota.webcore.utils.constant

import com.paveltsikota.webcore.utils.enums.HashType

object Constants {
    // Config
    const val DEFAULT_PROFILE = 1L

    val DEFAULT_CFG_HASH_DIR = "/${HashType.XXHASH64}"
    val DEFAULT_CFG_HASH_TYPE = HashType.XXHASH64
    const val DEFAULT_CFG_BUFFER_SIZE = 4_194_304L    // 4mb
    const val DEFAULT_CFG_PROGRESS_N = 500
    const val DEFAULT_CFG_PROGRESS_SIZE = 1_073_741_824L // 1gb
    const val DEFAULT_CFG_PROGRESS_SHOW = true
    const val DEFAULT_CFG_FLY_HASH_CALCULATE = true

    const val DEFAULT_PROFILE_TITLE = "Default"
    const val DEFAULT_PROFILE_DESCRIPTION = "Default profile (created automatically)"
}