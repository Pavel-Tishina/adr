package com.paveltsikota.webcore.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ObjUtilsTest {

    @Test
    fun `all is null test`() {
        assertTrue { ObjUtils.allIsNull(null) }
        assertTrue { ObjUtils.allIsNull(arrayOfNulls(5)) }
        assertTrue { ObjUtils.allIsNull(arrayOf(null, null)) }

        assertFalse { ObjUtils.allIsNull(arrayOf(5L)) }
        assertFalse { ObjUtils.allIsNull(arrayOf(null, "")) }
        assertFalse { ObjUtils.allIsNull(arrayOf(5L, "")) }
    }

    @Test
    fun `all is not null test`() {
        assertFalse { ObjUtils.allIsNotNull(null) }
        assertFalse { ObjUtils.allIsNotNull(arrayOfNulls(5)) }
        assertFalse { ObjUtils.allIsNotNull(arrayOf(null, null)) }
        assertFalse { ObjUtils.allIsNotNull(arrayOf(null, "")) }

        assertTrue { ObjUtils.allIsNotNull(arrayOf(5L)) }
        assertTrue { ObjUtils.allIsNotNull(arrayOf(5L, "")) }
    }

}