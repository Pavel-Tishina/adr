package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.db.dto.HistoryElementDto
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.enums.JobStatus
import jakarta.persistence.*

@Entity
@Table(name = "jobs")
data class JobsEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long = DEFAULT_PROFILE,

    @Column(nullable = true)
    val uuid: String? = null,

    @Column(nullable = false)
    val global: Boolean = false,

    @Column(nullable = true)
    var start: Long? = null,

    @Column(nullable = true)
    var finish: Long? = null,

    @Column(nullable = false)
    var disabled: Boolean = false,

    @Column(name = "taskList", nullable = false, columnDefinition = "TEXT")
    var taskList: MutableList<Long> = mutableListOf(),

    @Column(nullable = true, columnDefinition = "TEXT")
    @ElementCollection
    var history: MutableList<HistoryElementDto>? = null,

    @Column(nullable = false)
    var status: JobStatus = JobStatus.CREATED

): CommonEntity {

    override fun same(o: Any?): Boolean {
        return o is JobsEntity
                && profile == o.profile
                && disabled == o.disabled
                && start == o.start
                && finish == o.finish
                && status == o.status
                && uuid == o.uuid
                && taskList == o.taskList // CHK
                && history?.equals(o.history) == true // CHK
    }

}