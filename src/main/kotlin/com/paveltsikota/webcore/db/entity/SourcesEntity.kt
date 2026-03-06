package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import jakarta.persistence.*

@Entity
@Table(name = "sources")
data class SourcesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: IdType = 0,

    @Column(nullable = false)
    val profile: ProfileType = DEFAULT_PROFILE,

    @Column(nullable = false)
    var dirorder: DirOrderType = 0,

    @Column(nullable = false)
    var path: PathType = "",

): CommonEntity {

    override fun same(o: Any?): Boolean {
        return o is SourcesEntity
                && profile == o.profile
                && dirorder == o.dirorder
                && path == o.path
    }

}