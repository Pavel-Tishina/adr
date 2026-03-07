package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import jakarta.persistence.*

@Entity
@Table(name = "sources")
data class SourcesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long = DEFAULT_PROFILE,

    @Column(nullable = false)
    var dirorder: Int = 0,

    @Column(nullable = false)
    var path: String = "",

): CommonEntity {

    override fun same(o: Any?): Boolean {
        return o is SourcesEntity
                && profile == o.profile
                && dirorder == o.dirorder
                && path == o.path
    }

}