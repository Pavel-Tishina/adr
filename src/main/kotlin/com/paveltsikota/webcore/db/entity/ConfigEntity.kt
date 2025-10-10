package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_BUFFER_SIZE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_FLY_HASH_CALCULATE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_HASH_DIR
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_HASH_TYPE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_N
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_SHOW
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_SIZE
import com.paveltsikota.webcore.utils.enums.HashType
import jakarta.persistence.*

@Entity
data class ConfigEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long = DEFAULT_PROFILE,

    @Column(nullable = false)
    var hashDir: String = DEFAULT_CFG_HASH_DIR,

    @Column(nullable = false)
    var hashType: HashType = DEFAULT_CFG_HASH_TYPE,

    @Column(nullable = false)
    var bufferSize: Long = DEFAULT_CFG_BUFFER_SIZE,

    @Column(nullable = false)
    var progressN: Int = DEFAULT_CFG_PROGRESS_N,

    @Column(nullable = false)
    var progressSize: Long = DEFAULT_CFG_PROGRESS_SIZE,

    @Column(nullable = false)
    var progressShow: Boolean = DEFAULT_CFG_PROGRESS_SHOW,

    @Column(nullable = false)
    var flyHashCalculate: Boolean = DEFAULT_CFG_FLY_HASH_CALCULATE,

    )