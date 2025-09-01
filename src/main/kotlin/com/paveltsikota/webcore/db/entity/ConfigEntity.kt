package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.enums.HashType
import jakarta.persistence.*

@Entity
@Table(name = "config")
data class ConfigEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long,

    @Column(nullable = false)
    var hashDir: String,

    @Column(nullable = false)
    var hashType: HashType = HashType.XXHASH64,

    @Column(nullable = false)
    var bufferSize: Long = 4_194_304L, // 4mb

    @Column(nullable = false)
    var progressN: Int = 500,

    @Column(nullable = false)
    var progressSize: Long = 1_073_741_824L, // 1gb

    @Column(nullable = false)
    var progressShow: Boolean = true,

    @Column(nullable = false)
    var flyHashCalculate: Boolean = true,

)