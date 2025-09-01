package com.paveltsikota.webcore.utils.enums

enum class CryptoHashType(private val algorithm: String) {
    MD2("MD2"),
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    override fun toString(): String = algorithm
}