package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.enums.HashType
import jakarta.persistence.*

@Entity
@Table(name = "hashes")
data class HashesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long,

    @Column(nullable = false)
    val size: Long,

    @Column(nullable = false)
    val hash: String,

    @Column(nullable = false)
    val hashType: HashType,

    @Column(nullable = false)
    var main: Long,

    @Column(nullable = false)
    var duplicates: Set<Long>,

)