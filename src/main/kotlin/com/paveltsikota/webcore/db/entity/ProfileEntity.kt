package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.db.convertor.MapToJsonConverter
import jakarta.persistence.*

@Entity
@Table(name = "profile")
data class ProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 64)
    var title: String,

    @Column(nullable = true, length = 256)
    var description: String,

    @Convert(converter = MapToJsonConverter::class)
    @Column(nullable = false, columnDefinition = "TEXT") // Liquibase заменит на jsonb в PostgreSQL
    var cfg: String

)