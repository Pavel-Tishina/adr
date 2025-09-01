package com.paveltsikota.webcore.hash.calculator

import com.paveltsikota.webcore.hash.calculator.impl.Blake2B
import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import com.paveltsikota.webcore.utils.enums.Blake2BType
import org.junit.jupiter.api.Test
import kotlin.io.path.Path
import kotlin.test.Ignore
import kotlin.test.assertEquals

class HashCalculatorTest {
    val xxHash64 = XXHash64

    val content = "There is some data!"
    val xxHash64Hash100times = "3463856d2ab5c7d0"

    val blake160Hash3Times = "871e218c8a5fa88ea783ad3ee63fd95cce44d63b"
    val blake256Hash3Times = "fd508000d701925de8213a5a0bb3f8f55168230d7b85ab867bdc0b1befbd51fb"
    val blake384Hash3Times = "cc9cb5719c4294dcb0bba12a28486405056fc1cb08c4c0f274b5d5c331dc61882a2f0328d9569a7bde93bc84718a03bf"
    val blake512Hash3Times = "0af442fc467d6757ad000888249bb85e88e802067969cc3b3c7c08c7d9b747ec0ecd47ae0b0c6d44e2dc359ff8026b07cd3b875f54d1d89005bea58bfc50c108"

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

    @Test
    fun `blake test in memory`() {
        val content3 = content.repeat(3)

        val blake160 = Blake2B(Blake2BType.BLAKE2B_160)
        val blake256 = Blake2B(Blake2BType.BLAKE2B_256)
        val blake384 = Blake2B(Blake2BType.BLAKE2B_384)
        val blake512 = Blake2B(Blake2BType.BLAKE2B_512)

        assertEquals(blake160Hash3Times, blake160.calculate(stream = content3.byteInputStream()))
        assertEquals(blake256Hash3Times, blake256.calculate(stream = content3.byteInputStream()))
        assertEquals(blake384Hash3Times, blake384.calculate(stream = content3.byteInputStream()))
        assertEquals(blake512Hash3Times, blake512.calculate(stream = content3.byteInputStream()))
    }

}