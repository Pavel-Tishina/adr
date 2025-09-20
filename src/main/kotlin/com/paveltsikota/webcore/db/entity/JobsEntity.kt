package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "jobs")
data class JobsEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long = 0,

    @Column(nullable = false)
    val priority: Int = 0,

    @Column(nullable = true)
    var start: Timestamp? = null,

    @Column(nullable = true)
    var finish: Timestamp? = null,

    @Column(nullable = true)
    var completed: Boolean? = null,

    @Column(nullable = false)
    var disabled: Boolean = false,

    @Column(nullable = false)
    val type: JobsType,

    @Column(nullable = true)
    var lastObject: String? = null,

    @Column(nullable = true)
    var lastObjectId: Long? = null,

    @Column(nullable = false)
    var status: JobStatus = JobStatus.CREATED

    )