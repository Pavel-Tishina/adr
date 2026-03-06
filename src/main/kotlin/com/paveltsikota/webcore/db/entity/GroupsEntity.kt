package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import jakarta.persistence.*

@Entity
@Table(name = "groups")
data class GroupsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: IdType = 0,

    @Column(nullable = false)
    val profile: ProfileType = DEFAULT_PROFILE,

    @Column(nullable = false)
    val size: SizeType = 0,

    @Column(nullable = true)
    var fileIds: Set<Long> = hashSetOf(),

    @Column(name = "job_id", nullable = true)
    val jobId: JobIdType? = null,

): CommonEntity {

    override fun same(o: Any?): Boolean {
        return o is GroupsEntity
                && profile == o.profile
                && size == o.size
                && jobId == o.jobId
                && fileIds == o.fileIds
    }
}