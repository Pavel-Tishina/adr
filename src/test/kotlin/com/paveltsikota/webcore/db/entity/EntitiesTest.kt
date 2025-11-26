package com.paveltsikota.webcore.db.entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EntitiesTest {

    @Test
    fun `test equals of FilesEntity`() {
        val e1 = FilesEntity()
        val e2 = FilesEntity()

        assertEquals(e1, e2)
        assertTrue(e1 == e2)
    }

}