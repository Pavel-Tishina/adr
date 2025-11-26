package com.paveltsikota.webcore.utils.entity

import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.utils.HashesUtils.addDuplicate
import com.paveltsikota.webcore.db.utils.HashesUtils.addDuplicates
import com.paveltsikota.webcore.db.utils.HashesUtils.delDuplicate
import com.paveltsikota.webcore.db.utils.HashesUtils.delDuplicates
import com.paveltsikota.webcore.db.utils.HashesUtils.setMain
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class HashesEntityUtilsTest {
    private val originalMain = 4L
    private val afterSetMain = 1L

    private val afterDel1 = 2L
    private val afterDel2 = 3L

    private val originalSet = mutableSetOf<Long>(1,2,3)
    private val afterSetMainSet = mutableSetOf<Long>(2,3,4)
    private val afterAddDupSet1 = mutableSetOf<Long>(1,2,3,5)
    private val afterAddDupSet2 = mutableSetOf<Long>(1,2,3,5,6,7)

    private val afterDelDupSet1 = mutableSetOf<Long>(2,3)
    private val afterDelDupSet2 = mutableSetOf<Long>(3)
    private val afterDelDupSet3 = mutableSetOf<Long>()

    @Test
    fun `test set main`() {
        val original = HashesEntity(main = originalMain, duplicates = originalSet)
        val result = HashesEntity(main = afterSetMain, duplicates = afterSetMainSet)
        assertTrue(result == setMain(original, afterSetMain))
    }

    @Test
    fun `test add duplicate and duplicates`() {
        val original = HashesEntity(main = originalMain, duplicates = originalSet)
        val result1 = original.copy(duplicates = afterAddDupSet1)
        val result2 = original.copy(duplicates = afterAddDupSet2)

        assertTrue(original == addDuplicate(original, originalMain))
        assertTrue(original == addDuplicates(original, setOf(originalMain)))

        assertTrue(result1 == addDuplicate(original, 5L))
        assertTrue(result2 == addDuplicates(original, setOf(originalMain, 5L, 6L, 7L)))
    }

    @Test
    fun `test remove duplicate and duplicates`() {
        val original = HashesEntity(main = originalMain, duplicates = originalSet)
        val result1 = original.copy(duplicates = afterDelDupSet1)
        val result2 = original.copy(main = afterDel1, duplicates = afterDelDupSet2)
        val result3 = original.copy(main = afterDel2, duplicates = afterDelDupSet3)

        assertTrue(result1 == delDuplicate(original, 1L))
        assertTrue(result2 == delDuplicate(original, 4L))

        assertTrue(result2 == delDuplicates(original, setOf(1, 4)))
        assertTrue(result3 == delDuplicates(original, setOf(1, 2, 4)))
    }

}