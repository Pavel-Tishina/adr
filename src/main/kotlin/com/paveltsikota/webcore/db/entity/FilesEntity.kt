package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType
import jakarta.persistence.*

@Entity
@Table(name = "files")
data class FilesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "profile", nullable = false)
    val profile: Long = DEFAULT_PROFILE,

    @Column(name = "size", nullable = false)
    val size: Long = 0,

    @Column(name = "created", nullable = false)
    val created: Long = 0,

    @Column(name = "modified", nullable = false)
    val modified: Long = 0,

    @Column(name = "path", nullable = false)
    val path: String = "",

    @Column(name = "hashPath", nullable = true)
    var hashPath: String? = null,

    @Column(name = "fileName", nullable = false)
    val fileName: String = "",

    @Column(name = "newFileName", nullable = true)
    var newFileName: String? = null,

    @Column(name = "isUnique", nullable = true)
    var isUnique: Boolean? = null,

    @Column(name = "groupId", nullable = true)
    var groupId: Long? = null,

    @Column(name = "hashId", nullable = true)
    var hashId: Long? = null,

    @Column(name = "hash", nullable = true)
    var hash: String? = null,

    @Column(name = "hashType", nullable = true)
    var hashType: HashType? = null,

    @Column(name = "state", nullable = true)
    var state: FileState? = null,

    @Column(name = "hold", nullable = false)
    var hold: Boolean = false
)