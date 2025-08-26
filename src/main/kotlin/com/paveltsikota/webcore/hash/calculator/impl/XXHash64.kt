package com.paveltsikota.webcore.hash.calculator.impl

import com.paveltsikota.webcore.hash.calculator.HashCalculator
import com.paveltsikota.webcore.utils.enums.HashType
import net.jpountz.xxhash.XXHashFactory
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

class XXHash64: HashCalculator {
    private val factory = XXHashFactory.fastestInstance()
    private val bufferSize = 4096 * 1024

    /** Хэш файла xxHash64 (streaming), буфер по умолчанию 4 МБ. */
    fun ofFile(path: Path, seed: Long = 0L): Long {
        return ofStream(
            stream = Files.newInputStream(path),
            seed = seed
        )
    }

    fun ofStream(stream: InputStream, seed: Long = 0L): Long {
        val streaming = factory.newStreamingHash64(seed)

        stream.use { inStream ->
            val buf = ByteArray(bufferSize)
            while (true) {
                val n = inStream.read(buf)
                if (n <= 0) break
                streaming.update(buf, 0, n)
            }
        }
        return streaming.value
    }

    /** Хэш массива байт xxHash64. */
    fun ofBytes(bytes: ByteArray, seed: Long = 0L): Long {
        val hasher = factory.hash64()
        return hasher.hash(bytes, 0, bytes.size, seed)
    }

    /** Хэш строки (UTF-8) xxHash64. */
    fun ofString(s: String, seed: Long = 0L): Long =
        ofBytes(s.toByteArray(StandardCharsets.UTF_8), seed)

    /** Удобный хелпер: представить результат в hex. */
    fun toHex(hash: Long): String = java.lang.Long.toUnsignedString(hash, 16).padStart(16, '0')

    override fun calculate(path: Path): String {
        val hash = ofFile(path = path)
        return toHex(hash)
    }

    override fun calculate(stream: InputStream): String {
        val hash = ofStream(stream)
        return toHex(hash)
    }

    override fun getType(): HashType {
        return HashType.XXHASH64
    }

}