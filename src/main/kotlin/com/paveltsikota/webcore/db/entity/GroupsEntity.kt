package com.paveltsikota.webcore.db.entity

import jakarta.persistence.*

@Entity
@Table(name = "groups")
data class GroupsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long,

    @Column(nullable = false)
    val size: Long,

    @Column(nullable = false)
    var fileIds: Set<Long>,
    
)