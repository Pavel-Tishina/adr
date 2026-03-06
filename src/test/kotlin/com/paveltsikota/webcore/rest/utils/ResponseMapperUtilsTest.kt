package com.paveltsikota.webcore.rest.utils

import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.entity.JobsTaskEntity
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.db.adapter.FilesAdapter
import com.paveltsikota.webcore.db.adapter.HashesAdapter
import com.paveltsikota.webcore.db.adapter.JobsTaskAdapter
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
            fileIds = setOf(11,22,33),
            jobId = null
        )

        val input = listOf(
            mapOf(
                JobsTaskEntity() to "ololo",
                "trololo" to HashesEntity(),
                HashType.XXHASH64 to mapOf<Any, Any>(
                    123 to listOf(false, true, false)
                )
            ),
            setOf(
                123,
                false,
                FilesEntity()
            ),
            "trololo",
            SourcesEntity(),
            dto
        )

        val expectedResult = listOf(
            mapOf(
                JobsTaskAdapter.entityToDto(JobsTaskEntity()) to "ololo",
                "trololo" to HashesAdapter.entityToDto(HashesEntity()),
                HashType.XXHASH64 to mapOf<Any, Any>(
                    123 to listOf(false, true, false)
                )
            ),
            setOf(
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

//    private fun formalEq(e1: List<Any>, e2: List<Any>): Boolean {
//        return if (e1.size != e2.size) {
//            false
//        } else {
//            for (item in e1)
//        }
//    }
}