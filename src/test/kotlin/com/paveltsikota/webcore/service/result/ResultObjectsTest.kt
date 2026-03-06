package com.paveltsikota.webcore.service.result

import com.paveltsikota.webcore.db.service.result.ResultObjects
import com.paveltsikota.webcore.db.service.result.enums.ResultObjectSetType
import com.paveltsikota.webcore.db.service.result.enums.ResultObjectSetType.*
import org.junit.jupiter.api.assertNull
import org.mockito.ArgumentMatchers.anyString
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ResultObjectsTest {

    val strings = listOf("one", "two", "three", "four", "five", "six")

    @Test
    fun `just after init`() {
        val result = ResultObjects<String>()

        assertFalse {
            result.hasProblems()
            result.isSuccess()
            result.isBad()

            result.isNotEmpty(GOOD)
            result.isNotEmpty(WARN)
            result.isNotEmpty(ERROR)

            result.contains(anyString(), GOOD)
            result.contains(anyString(), WARN)
            result.contains(anyString(), ERROR)

            result.isNotEmpty(GOOD)
            result.isNotEmpty(WARN)
            result.isNotEmpty(ERROR)
        }

        assertTrue {
            result.containsAll(listOf(), GOOD)
            result.containsAll(listOf(), WARN)
            result.containsAll(listOf(), ERROR)
        }

        assertFalse {
            result.containsAll(strings, GOOD)
            result.containsAll(strings, WARN)
            result.containsAll(strings, ERROR)
        }

        assertFalse {
            result.removeAll(strings, GOOD)
            result.removeAll(strings, WARN)
            result.removeAll(strings, ERROR)
        }

        assertFalse {
            result.remove(anyString(), GOOD)
            result.remove(anyString(), WARN)
            result.remove(anyString(), ERROR)
        }

        assertNull(result.getNullIfEmpty(GOOD))
        assertNull(result.getNullIfEmpty(WARN))
        assertNull(result.getNullIfEmpty(ERROR))

        assertContentEquals(emptyList(), result.getList(GOOD))
        assertContentEquals(emptyList(), result.getList(WARN))
        assertContentEquals(emptyList(), result.getList(ERROR))
    }

    @Test
    fun `after add some GOOD`() {
        val result = ResultObjects<String>()
        val listWithT = strings.filter { it.contains("t") }

        // add some GOOD
        assertTrue{
            result.add(anyString(), GOOD)
            result.addAll(listWithT, GOOD)
        }

        assertTrue{
            result.isSuccess()
            result.isNotEmpty(GOOD)

            result.contains(listWithT[0], GOOD)
            result.containsAll(listWithT, GOOD)
        }

        // check add again existed values, remove unexisted and others
        assertFalse{
            result.isBad()
            result.hasProblems()

            result.isNotEmpty(WARN)
            result.isNotEmpty(ERROR)

            result.contains(listWithT[0], WARN)
            result.containsAll(listWithT, WARN)

            result.contains(listWithT[0], ERROR)
            result.containsAll(listWithT, ERROR)
        }

        assertContentEquals(listOf(anyString()).plus(listWithT), result.getList(GOOD))
        assertContentEquals(emptyList(), result.getList(WARN))
        assertContentEquals(emptyList(), result.getList(ERROR))

        assertTrue {
            result.remove(anyString(), GOOD)
        }

        assertFalse {
            result.contains(anyString(), GOOD)

            result.remove(anyString(), GOOD)
            result.remove(anyString(), WARN)
            result.remove(anyString(), ERROR)

            result.removeAll(strings, WARN)
            result.removeAll(strings, ERROR)
        }
    }

    @Test
    fun `after add some GOOD and WARN`() {
        val result = ResultObjects<String>()
        val listWithT = strings.filter { it.contains("t") }

        // add some GOOD
        assertTrue{
            result.add(anyString(), WARN)
            result.addAll(listWithT, GOOD)
        }

        assertTrue{
            result.isSuccess()
            result.hasProblems()
            result.isNotEmpty(GOOD)
            result.isNotEmpty(WARN)

            result.contains(anyString(), WARN)
            result.contains(listWithT[0], GOOD)
            result.containsAll(listWithT, GOOD)
        }

        // check add again existed values, remove unexisted and others
        assertFalse{
            result.isBad()
            result.isNotEmpty(ERROR)

            result.contains(anyString(), GOOD)

            result.contains(listWithT[0], WARN)
            result.containsAll(listWithT, WARN)

            result.contains(listWithT[0], ERROR)
            result.containsAll(listWithT, ERROR)
        }

        assertContentEquals(listWithT, result.getList(GOOD))
        assertContentEquals(listOf(anyString()), result.getList(WARN))
        assertContentEquals(emptyList(), result.getList(ERROR))

        assertTrue {
            result.remove(anyString(), WARN)
            result.isSuccess()
        }

        assertFalse {
            result.isNotEmpty(WARN)

            result.remove(anyString(), GOOD)
            result.remove(anyString(), WARN)
            result.remove(anyString(), ERROR)

            result.removeAll(strings, WARN)
            result.removeAll(strings, ERROR)
        }
    }

    @Test
    fun `now add some ERROR`() {
        val result = ResultObjects<String>()
        val listWithT = strings.filter { it.contains("t") }

        assertTrue{
            result.add(anyString(), WARN)
            result.add(anyString(), ERROR)
            result.add(listWithT[0], ERROR)
            result.add(anyString(), ERROR)
            result.addAll(listWithT, GOOD)
        }

        assertTrue {
            result.isNotEmpty(GOOD)
            result.isNotEmpty(WARN)
            result.isNotEmpty(ERROR)

            result.isSuccess()
            result.hasProblems()
        }

        assertFalse(result.isBad())

        // now remove from good and get BAD
        assertTrue(result.removeAll(strings, GOOD))
        assertTrue(result.isBad())
        assertFalse(result.hasProblems())

        assertNull(result.getNullIfEmpty(GOOD))
    }

    @Test
    fun `test clear`() {
        val result = ResultObjects<String>()
        result.addAll(strings, ERROR)
        result.addAll(strings, GOOD)

        assertTrue(result.hasProblems())

        result.clear(ERROR)

        assertFalse(result.hasProblems())
    }

}