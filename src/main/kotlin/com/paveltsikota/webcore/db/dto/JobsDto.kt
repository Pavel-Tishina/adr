package com.paveltsikota.webcore.db.dto

import com.paveltsikota.webcore.db.types.DataTypeAlias.*
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.enums.JobStatus

data class JobsDto (
    val id: IdType? = 0,
    val profile: ProfileType = DEFAULT_PROFILE,
    val uuid: UuidType? = null,       // if exist => job collapse for
    val global: GlobalType = false,    // if true => run over all entities from profile
    var start: StartTimestampType? = null,
    var finish: StartTimestampType? = null,
    var disabled: DisabledType = false,
    var taskList: MutableList<Long> = mutableListOf(),
    var history: MutableList<HistoryElementDto>? = null,
    var status: JobStatus = JobStatus.CREATED,
    var runningTime: String? = null,
): CommonDto