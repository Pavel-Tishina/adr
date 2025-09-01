package com.paveltsikota.webcore.hash.calculator.impl


import com.paveltsikota.webcore.hash.calculator.HashCalculator
import com.paveltsikota.webcore.utils.enums.CryptoHashType
import com.paveltsikota.webcore.utils.enums.HashType
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest


class CryptoHash(cryptoHashType: CryptoHashType): HashCalculator {
    private val hashType = when (cryptoHashType) {
        CryptoHashType.MD2 -> HashType.MD2
        CryptoHashType.MD5 -> HashType.MD5
        CryptoHashType.SHA1 -> HashType.SHA1
        CryptoHashType.SHA256 -> HashType.SHA256
        CryptoHashType.SHA384 -> HashType.SHA384
        CryptoHashType.SHA512 -> HashType.SHA512
    }
    private val calc = MessageDigest.getInstance(hashType.toString())
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
                calc.update(buf, 0, n)
            }
        }
        return calc.digest()
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