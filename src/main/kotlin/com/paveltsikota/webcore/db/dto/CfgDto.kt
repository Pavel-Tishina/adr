package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_BUFFER_SIZE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_FLY_HASH_CALCULATE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_HASH_DIR
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_HASH_TYPE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_N
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_SHOW
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_SIZE
import com.paveltsikota.webcore.utils.enums.HashType

data class CfgDto(
    val hashDir: HashPathType = DEFAULT_CFG_HASH_DIR,
    val hashType: HashType = DEFAULT_CFG_HASH_TYPE,
    val bufferSize: BufferSizeType = DEFAULT_CFG_BUFFER_SIZE,
    val progressN: ProgressNType = DEFAULT_CFG_PROGRESS_N,
    val progressSize: ProgressSizeType = DEFAULT_CFG_PROGRESS_SIZE,
    val progressShow: ProgressShowType = DEFAULT_CFG_PROGRESS_SHOW,
    val flyHashCalculate: FlyHashCalculateType = DEFAULT_CFG_FLY_HASH_CALCULATE,
): CommonDto