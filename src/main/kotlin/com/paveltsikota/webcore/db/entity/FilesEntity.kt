package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType
import jakarta.persistence.*

@Entity
@Table(name = "files")
data class FilesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long,

    @Column(nullable = false)
    val size: Long,

    @Column(nullable = false)
    val created: Long,

    @Column(nullable = false)
    val modified: Long,

    @Column(nullable = false)
    val path: String,

    @Column(nullable = true)
    var hashPath: String,

    @Column(nullable = false)
    val fileName: String,

    @Column(nullable = true)
    var newFileName: String,

    @Column(nullable = true)
    var isUnique: Boolean,

    @Column(nullable = true)
    var groupId: Long,

    @Column(nullable = true)
    var hashId: Long,

    @Column(nullable = true)
    var hash: String,

    @Column(nullable = true)
    var hashType: HashType,

    @Column(nullable = true)
    var state: FileState = FileState.ON_PLACE,

    @Column(nullable = false)
    var hold: Boolean = false
)