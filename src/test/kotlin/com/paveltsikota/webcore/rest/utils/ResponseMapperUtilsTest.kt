package com.paveltsikota.webcore.rest.utils

import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.entity.JobsEntity
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.utils.entity.FilesEntityUtils
import com.paveltsikota.webcore.utils.entity.HashesEntityUtils
import com.paveltsikota.webcore.utils.entity.JobEntityUtils
import com.paveltsikota.webcore.utils.entity.SourcesEntityUtils
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
                JobEntityUtils.entityToDto(JobsEntity()) to "ololo",
                "trololo" to HashesEntityUtils.entityToDto(HashesEntity()),
                HashType.XXHASH64 to mapOf<Any, Any>(
                    123 to listOf(false, true, false)
                )
            ),
            setOf<Any>(
                123,
                false,
                FilesEntityUtils.entityToDto(FilesEntity())
            ),
            "trololo",
            SourcesEntityUtils.entityToDto(SourcesEntity()),
            dto
        )

        assertEquals(expectedResult, ResponseMapperUtils.anyToDto(input))
    }
}