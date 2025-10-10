package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import jakarta.persistence.*

@Entity
@Table(name = "groups")
data class GroupsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long = DEFAULT_PROFILE,

    @Column(nullable = false)
    val size: Long = 0,

    @Column(nullable = true)
    var fileIds: Set<Long> = hashSetOf(),

    )