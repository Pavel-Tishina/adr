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
    val profile: Long = 0,

    @Column(nullable = false)
    val size: Long = 0,

    @Column(nullable = false)
    val hash: String = "",

    @Column(nullable = false)
    val hashType: HashType = HashType.UNKNOWN,

    @Column(nullable = false)
    var main: Long = 0,

    @Column(nullable = false)
    var duplicates: MutableSet<Long> = hashSetOf(),

    )