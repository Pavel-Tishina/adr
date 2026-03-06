package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.db.convertor.JobHistoryToJsonConverter
import com.paveltsikota.webcore.db.convertor.MutableListToJsonConverter
import com.paveltsikota.webcore.db.dto.HistoryElementDto
import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.enums.JobStatus
import jakarta.persistence.*

// re-implement as JobsEntity
@Entity
@Table(name = "jobs")
data class JobsEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: IdType = 0,

    @Column(nullable = false)
    val profile: ProfileType = DEFAULT_PROFILE,

    @Column(nullable = true)
    val uuid: UuidType? = null,

    @Column(nullable = false)
    val global: GlobalType = false,

    @Column(nullable = true)
    var start: StartDateType? = null,

    @Column(nullable = true)
    var finish: FinishDateType? = null,

    @Column(nullable = false)
    var disabled: DisabledType = false,

    @Convert(converter = MutableListToJsonConverter::class)
    @Column(nullable = false)
    var taskList: MutableList<Long> = mutableListOf(),

    @Convert(converter = JobHistoryToJsonConverter::class)
    @Column(nullable = true)
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