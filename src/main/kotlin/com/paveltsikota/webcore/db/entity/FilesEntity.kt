package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.FileUtils
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.enums.FileState
import com.paveltsikota.webcore.utils.enums.HashType
import jakarta.persistence.*
import kotlin.io.path.Path

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
    var hold: HoldType = false,

    @Column(name = "jobId", nullable = true)
    val jobId: JobIdType? = null,

    //TODO: add fast-hash for first 2-4-8-32-64-128kb and 4-8-16mb for videos and big files
    //TODO: add for all entities archive/restore as Long
): CommonEntity {

    override fun equals(o: Any?): Boolean = o is FilesEntity && id == o.id && same(o)

    override fun same(o: Any?): Boolean {
        return o is FilesEntity
                && state == o.state
                && hold == o.hold
                && isUnique == o.isUnique
                && profile == o.profile
                && size == o.size
                && created == o.created
                && modified == o.modified
                && hashId == o.hashId
                && groupId == o.groupId
                && hashType == o.hashType
                && fileName == o.fileName
                && newFileName == o.newFileName
                && FileUtils.toUnixPath(Path(path)) == FileUtils.toUnixPath(Path(o.path))
    }

}