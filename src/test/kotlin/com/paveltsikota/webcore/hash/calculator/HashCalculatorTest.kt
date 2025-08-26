package com.paveltsikota.webcore.hash.calculator

import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import org.junit.jupiter.api.Test
import kotlin.io.path.Path
import kotlin.test.Ignore
import kotlin.test.assertEquals

class HashCalculatorTest {
    val xxHash64 = XXHash64()

    val content = "There is some data!"
    val xxHash64Hash100times = "3463856d2ab5c7d0"

    @Test
    @Ignore
    fun `xxhash64 test`() {
        val result = xxHash64.calculate(path = Path("F:/ip_addresses.7z"))
        assertEquals("5ce7d2f123693b3c", result)
    }

    @Test
    fun `xxhash64 test in memory`() {
        val content100 = content.repeat(100)
        assertEquals(xxHash64Hash100times, xxHash64.calculate(stream = content100.byteInputStream()))
    }




}