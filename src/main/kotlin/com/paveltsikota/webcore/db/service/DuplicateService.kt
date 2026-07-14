package com.paveltsikota.webcore.db.service

import com.paveltsikota.webcore.db.dto.DuplicateDto
import com.paveltsikota.webcore.db.service.result.DuplicateServiceOperationResult

interface DuplicateService {

    fun getDuplicates(count: Int, page: Int, profileId: Long, dupN: Int): DuplicateServiceOperationResult
    fun updateDuplicates(duplicates: List<DuplicateDto>, profileId: Long): DuplicateServiceOperationResult

}