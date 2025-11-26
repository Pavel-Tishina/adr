package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.utils.enums.JobStatus
import com.paveltsikota.webcore.utils.enums.JobsType
import java.sql.Timestamp

data class JobsDto (
    val id: Long? = 0,
    val profile: Long = 0,
    val priority: Int? = null,
    var start: Timestamp? = null,
    var finish: Timestamp? = null,
    var completed: Boolean? = null,
    var disabled: Boolean = false,
    val type: JobsType,
    var lastObject: String? = null,
    var lastObjectId: Long? = null,
    var status: JobStatus = JobStatus.CREATED
): CommonDto