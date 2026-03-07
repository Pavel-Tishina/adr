package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.enums.JobStatus
import java.sql.Timestamp

data class JobsDto (
    val id: Long? = 0,
    val profile: Long = DEFAULT_PROFILE,
    val uuid: String? = null,       // if exist => job collapse for
    val global: Boolean = false,    // if true => run over all entities from profile
    var start: Timestamp? = null,
    var finish: Timestamp? = null,
    var disabled: Boolean = false,
    var taskList: MutableList<Long> = mutableListOf(),
    var history: MutableList<HistoryElementDto>? = null,
    var status: JobStatus = JobStatus.CREATED,
    var runningTime: String? = null,
): CommonDto