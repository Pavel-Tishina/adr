package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType

data class JobsTaskDto (
    val id: IdType? = 0,
    val profile: ProfileType = 0,
    val priority: PriorityType? = null,
    val jobId: JobIdType? = null,
    var start: StartTimestampType? = null,
    var finish: FinishTimestampType? = null,
    var disabled: DisabledType = false,
    val type: JobsType,
    var lastObjectId: LastObjectIdType? = null,
    var objects: MutableList<Long>? = null,
    var history: MutableList<HistoryElementDto>? = null,
    var status: JobStatus = JobStatus.CREATED,
    var runningTime: RunningTimeType? = null,
): CommonDto