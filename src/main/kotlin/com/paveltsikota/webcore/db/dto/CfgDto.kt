package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_BUFFER_SIZE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_FLY_HASH_CALCULATE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_HASH_DIR
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_HASH_TYPE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_N
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_SHOW
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_SIZE
import com.paveltsikota.webcore.utils.enums.HashType

data class CfgDto(
    val id: Long = 0,
    val hashDir: String = DEFAULT_CFG_HASH_DIR,
    val hashType: HashType = DEFAULT_CFG_HASH_TYPE,
    val bufferSize: Long = DEFAULT_CFG_BUFFER_SIZE,
    val progressN: Int = DEFAULT_CFG_PROGRESS_N,
    val progressSize: Long = DEFAULT_CFG_PROGRESS_SIZE,
    val progressShow: Boolean = DEFAULT_CFG_PROGRESS_SHOW,
    val flyHashCalculate: Boolean = DEFAULT_CFG_FLY_HASH_CALCULATE,
)