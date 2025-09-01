package com.paveltsikota.webcore.performance

import com.paveltsikota.webcore.hash.calculator.HashCalculator
import com.paveltsikota.webcore.hash.calculator.impl.Blake2B
import com.paveltsikota.webcore.hash.calculator.impl.CryptoHash
import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import com.paveltsikota.webcore.utils.enums.Blake2BType
import com.paveltsikota.webcore.utils.enums.CryptoHashType
import kotlin.test.Test
import kotlin.time.measureTimedValue

class HashTimingsTest {
    val allocateMemory = 4_294_967_296L // 4gb
    val maxObjects = 100000
    val objects = hashSetOf<String>()
    val t = 5

    val blake2b160 = Blake2B(Blake2BType.BLAKE2B_160)
    val blake2b256 = Blake2B(Blake2BType.BLAKE2B_256)
    val blake2b384 = Blake2B(Blake2BType.BLAKE2B_384)
    val blake2b512 = Blake2B(Blake2BType.BLAKE2B_512)

    val xxhash64 = XXHash64

    val md2 = CryptoHash(CryptoHashType.MD2)
    val md5 = CryptoHash(CryptoHashType.MD5)
    val sha1 = CryptoHash(CryptoHashType.SHA1)
    val sha256 = CryptoHash(CryptoHashType.SHA256)
    val sha384 = CryptoHash(CryptoHashType.SHA384)
    val sha512 = CryptoHash(CryptoHashType.SHA512)

    @Test
    fun `speed test with extra small files`() {
        val extraSmallStrings = createObjectsForCollision()
        println("ok, we gotcha ${extraSmallStrings.size} extra small stings. Let start calculate with $t times")

        speedTestCaclucation(blake2b160, extraSmallStrings)
        speedTestCaclucation(blake2b256, extraSmallStrings)
        speedTestCaclucation(blake2b384, extraSmallStrings)
        speedTestCaclucation(blake2b512, extraSmallStrings)
        speedTestCaclucation(xxhash64, extraSmallStrings)
        speedTestCaclucation(md2, extraSmallStrings)
        speedTestCaclucation(md5, extraSmallStrings)
        speedTestCaclucation(sha1, extraSmallStrings)
        speedTestCaclucation(sha256, extraSmallStrings)
        speedTestCaclucation(sha384, extraSmallStrings)
        speedTestCaclucation(sha512, extraSmallStrings)
    }

    @Test
    fun `collision test with extra small files`() {
        val extraSmallStrings = createObjectsForCollision()
        println("ok, we gotcha ${extraSmallStrings.size} extra small stings. Let start check collision")

        calculateCollisions(blake2b160, extraSmallStrings)
        calculateCollisions(blake2b256, extraSmallStrings)
        calculateCollisions(blake2b384, extraSmallStrings)
        calculateCollisions(blake2b512, extraSmallStrings)
        calculateCollisions(xxhash64, extraSmallStrings)
        calculateCollisions(md2, extraSmallStrings)
        calculateCollisions(md5, extraSmallStrings)
        calculateCollisions(sha1, extraSmallStrings)
        calculateCollisions(sha256, extraSmallStrings)
        calculateCollisions(sha384, extraSmallStrings)
        calculateCollisions(sha512, extraSmallStrings)
    }

    private fun speedTestCaclucation(calc: HashCalculator, strings: Collection<String>) {
        println("${calc.getType()} start")
        val time = measureTimedValue {
            for (i in 0..< t)
                strings.forEach { calc.calculate(it) }
        }
        println("${calc.getType()} approximately take ${getHoursMinutesSecondsTime(time.duration.inWholeMilliseconds / t)}")
    }

    private fun calculateCollisions(calc: HashCalculator, strings: Collection<String>) {
        println("${calc.getType()} start")
        var collisions = 0
        var collisionTotal = 0
        val results = hashMapOf<String, Int>()

        strings.forEach {
            val r = blake2b256.calculate(it)
            results[r] = (results.getOrDefault(r, 0) + 1)
        }
        results.values.forEach{ i ->
            if (i > 1) {
                collisions++
                collisionTotal += i
            }
        }
        println("${calc.getType()} has $collisions collisions $collisionTotal times")
    }

    // 65792 elements, around 2gb
    private fun createObjectsForCollision(): HashSet<String> {
        val out = hashSetOf<String>()
        val chars = (0..255).map { it.toChar() }

        println("add 1-character strings")
        for (c in chars)
            out.add(c.toString())

        println("add 2-character strings")
        for (c1 in chars)
            for (c2 in chars)
                out.add("$c1$c2")

//        println("add 3-character strings")
//        for (c1 in chars)
//            for (c2 in chars)
//                for (c3 in chars)
//                    out.add("$c1$c2$c3")

//        println("add 4-character strings (not full)")
//        for (c1 in chars.subList(0, 80))
//            for (c2 in chars.subList(0, 80))
//                for (c3 in chars.subList(0, 80))
//                    for (c4 in chars.subList(0, 80))
//                        out.add("$c1$c2$c3$c4")

        println("done")
        return out
    }

    private fun getHoursMinutesSecondsTime(millis: Long): String {
        val hours = millis / 3_600_000
        val minutes = (millis % 3_600_000) / 60_000
        val seconds = (millis % 60_000) / 1000
        val ms = millis % 1000
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, ms)
    }

}