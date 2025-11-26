package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import jakarta.persistence.*

@Entity
@Table(name = "jobs")
data class JobsEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long = DEFAULT_PROFILE,

    @Column(nullable = false)
    val priority: Int = 0,

    @Column(nullable = true)
    var start: Long? = null,

    @Column(nullable = true)
    var finish: Long? = null,

    @Column(nullable = true)
    var completed: Boolean? = null,

    @Column(nullable = false)
    var disabled: Boolean = false,

    @Column(nullable = false)
    val type: JobsType = JobsType.FILE_SCAN,

    @Column(name = "lastObject", nullable = true)
    var lastObject: String? = null,

    @Column(name = "lastObjectId", nullable = true)
    var lastObjectId: Long? = null,

    @Column(nullable = false)
    var status: JobStatus = JobStatus.CREATED

): CommonEntity {

    override fun same(o: Any?): Boolean {
        return o is JobsEntity
                && profile == o.profile
                && completed == o.completed
                && disabled == o.disabled
                && priority == o.priority
                && start == o.start
                && finish == o.finish
                && type == o.type
                && status == o.status
                && lastObjectId == o.lastObjectId
                && lastObject == o.lastObject
    }

}