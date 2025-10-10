package com.paveltsikota.webcore.rest.utils

import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.db.adapter.FilesAdapter
import com.paveltsikota.webcore.db.adapter.HashesAdapter
import com.paveltsikota.webcore.db.adapter.JobsAdapter
import com.paveltsikota.webcore.db.adapter.SourcesAdapter
import com.paveltsikota.webcore.utils.enums.HashType
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseMapperUtilsTest {
    @Test
    fun `all types mapper`() {
        val dto = GroupsDto(
            id = 666,
            profile = 999,
            size = 777,
            fileIds = setOf(11,22,33)
        )

        val input = listOf<Any>(
            mapOf<Any, Any>(
                JobsEntity() to "ololo",
                "trololo" to HashesEntity(),
                HashType.XXHASH64 to mapOf<Any, Any>(
                    123 to listOf(false, true, false)
                )
            ),
            setOf<Any>(
                123,
                false,
                FilesEntity()
            ),
            "trololo",
            SourcesEntity(),
            dto
        )

        val expectedResult = listOf<Any>(
            mapOf<Any, Any>(
                JobsAdapter.entityToDto(JobsEntity()) to "ololo",
                "trololo" to HashesAdapter.entityToDto(HashesEntity()),
                HashType.XXHASH64 to mapOf<Any, Any>(
                    123 to listOf(false, true, false)
                )
            ),
            setOf<Any>(
                123,
                false,
                FilesAdapter.entityToDto(FilesEntity())
            ),
            "trololo",
            SourcesAdapter.entityToDto(SourcesEntity()),
            dto
        )

        assertEquals(expectedResult, ResponseMapperUtils.anyToDto(input))
    }
}