package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import java.sql.Timestamp

data class JobsTaskDto (
    val id: Long? = 0,
    val profile: Long = 0,
    val priority: Int? = null,
    val jobId: Long? = null,
    var start: Timestamp? = null,
    var finish: Timestamp? = null,
    var disabled: Boolean = false,
    val type: JobsType,
    var lastObjectId: Long? = null,
    var objects: MutableList<Long>? = null,
    var history: MutableList<HistoryElementDto>? = null,
    var status: JobStatus = JobStatus.CREATED,
    var runningTime: String? = null,
): CommonDto