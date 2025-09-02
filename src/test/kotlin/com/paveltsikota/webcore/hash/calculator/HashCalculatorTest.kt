package com.paveltsikota.webcore.hash.calculator

import com.paveltsikota.webcore.hash.calculator.impl.Blake2B
import com.paveltsikota.webcore.hash.calculator.impl.CryptoHash
import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import com.paveltsikota.webcore.utils.enums.Blake2BType
import com.paveltsikota.webcore.utils.enums.CryptoHashType
import org.junit.jupiter.api.Disabled
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

    val md2Hash3Times = "0a671725fc9fccba1c29ccbb84741194"
    val md5Hash3Times = "3dab4d433575ead5362c3c7bf4db6165"
    val sha1Hash3Times = "362f12697c8ebb389829828ac34f12286fe14e8d"
    val sha256Hash3Times = "9df22a4bd5a3fe00898c2bd2480bd683e06463277b8c671e328abb6586711ed0"
    val sha384Hash3Times = "66b9fbca74b2a8325c6f434cbaf625e4099a2e2b92c2c939512ee4d9bf44843f3f4b6d32a4e2a23422d52768bec981f0"
    val sha512Hash3Times = "ec49c9466f20dfa12d47552e382df10d6858ec644d435c266f4bb2b2884c9854b8c99b06bca955b8f61b60ee945d463cc32ae3b052bebeeecb6aedb59efad004"

    @Test
    @Disabled
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

    @Test
    fun `crypto hash test in memory`() {
        val content3 = content.repeat(3)

        val md2 = CryptoHash(CryptoHashType.MD2)
        val md5 = CryptoHash(CryptoHashType.MD5)
        val sha1 = CryptoHash(CryptoHashType.SHA1)
        val sha256 = CryptoHash(CryptoHashType.SHA256)
        val sha384 = CryptoHash(CryptoHashType.SHA384)
        val sha512 = CryptoHash(CryptoHashType.SHA512)

        assertEquals(md2Hash3Times, md2.calculate(content3))
        assertEquals(md5Hash3Times, md5.calculate(content3))
        assertEquals(sha1Hash3Times, sha1.calculate(content3))
        assertEquals(sha256Hash3Times, sha256.calculate(content3))
        assertEquals(sha384Hash3Times, sha384.calculate(content3))
        assertEquals(sha512Hash3Times, sha512.calculate(content3))
    }

}