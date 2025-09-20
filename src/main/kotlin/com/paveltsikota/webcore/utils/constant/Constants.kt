package com.paveltsikota.webcore.utils.constant

import com.paveltsikota.webcore.utils.enums.HashType

object Constants {
    // Config
    val DEFAULT_CFG_PROFILE = 0L
    val DEFAULT_CFG_HASH_DIR = "/${HashType.XXHASH64}"
    val DEFAULT_CFG_HASH_TYPE = HashType.XXHASH64
    val DEFAULT_CFG_BUFFER_SIZE = 4_194_304L    // 4mb
    val DEFAULT_CFG_PROGRESS_N = 500
    val DEFAULT_CFG_PROGRESS_SIZE = 1_073_741_824L // 1gb
    val DEFAULT_CFG_PROGRESS_SHOW = true
    val DEFAULT_CFG_FLY_HASH_CALCULATE = true

}