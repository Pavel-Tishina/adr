package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.db.convertor.JobHistoryToJsonConverter
import com.paveltsikota.webcore.db.convertor.MutableListToJsonConverter
import com.paveltsikota.webcore.db.dto.HistoryElementDto
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import jakarta.persistence.*

@Entity
@Table(name = "jobstask")
data class JobsTaskEntity (
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

    @Column(nullable = false)
    var disabled: Boolean = false,

    @Column(nullable = false)
    val type: JobsType = JobsType.FILE_SCAN,

    @Column(name = "lastObjectId", nullable = true)
    var lastObjectId: Long? = null,

    @Convert(converter = MutableListToJsonConverter::class)
    @Column(nullable = true)
    var objects: MutableList<Long>? = null,

    @Convert(converter = JobHistoryToJsonConverter::class)
    @Column(nullable = true)
    var history: MutableList<HistoryElementDto>? = null,

    @Column(nullable = false)
    var status: JobStatus = JobStatus.CREATED,

    @Column(name = "jobId", nullable = false)
    var jobId: Long? = null,

): CommonEntity {

    override fun same(o: Any?): Boolean {
        return o is JobsTaskEntity
                && profile == o.profile
                && jobId == o.jobId
                && disabled == o.disabled
                && priority == o.priority
                && start == o.start
                && finish == o.finish
                && type == o.type
                && status == o.status
                && lastObjectId == o.lastObjectId
                && objects?.equals(o.objects) == true // CHK
                && history?.equals(o.history) == true // CHK
    }

}