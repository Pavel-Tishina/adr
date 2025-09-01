package com.paveltsikota.webcore.hash.calculator.impl


import com.paveltsikota.webcore.hash.calculator.HashCalculator
import com.paveltsikota.webcore.utils.enums.Blake2BType
import com.paveltsikota.webcore.utils.enums.HashType
import com.rfksystems.blake2b.security.Blake2b160Digest
import com.rfksystems.blake2b.security.Blake2bProvider
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path


class Blake2B(blakeType: Blake2BType): HashCalculator {
    private val hashType = when (blakeType) {
        Blake2BType.BLAKE2B_160 -> HashType.BLAKE2B160
        Blake2BType.BLAKE2B_256 -> HashType.BLAKE2B256
        Blake2BType.BLAKE2B_384 -> HashType.BLAKE2B384
        Blake2BType.BLAKE2B_512 -> HashType.BLAKE2B512
    }
    private val blake = Blake2b160Digest.getInstance(blakeType.toString(), Blake2bProvider())
    private var bufferSize = 4096 * 1024

    private fun ofFile(path: Path): ByteArray {
        return ofStream(Files.newInputStream(path))
    }

    private fun ofStream(stream: InputStream): ByteArray {
        stream.use { inStream ->
            val buf = ByteArray(bufferSize)
            while (true) {
                val n = inStream.read(buf)
                if (n <= 0) break
                blake.update(buf, 0, n)
            }
        }
        return blake.digest()
    }

    private fun toHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    override fun calculate(path: Path): String {
        val hash = ofFile(path = path)
        return toHex(hash)
    }

    override fun calculate(stream: InputStream): String {
        val hash = ofStream(stream)
        return toHex(hash)
    }

    override fun calculate(str: String): String {
        return calculate(str.byteInputStream())
    }

    override fun getType(): HashType {
        return hashType
    }

}