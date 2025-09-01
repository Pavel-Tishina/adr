package com.paveltsikota.webcore.db.entity

import jakarta.persistence.*

@Entity
@Table(name = "sources")
data class SourcesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long,

    @Column(nullable = false)
    var dirorder: Int,

    @Column(nullable = false)
    var path: String,

)