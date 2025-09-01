package com.paveltsikota.webcore.utils.enums

enum class Blake2BType(private val algorithm: String) {
    BLAKE2B_160("BLAKE2B-160"),
    BLAKE2B_256("BLAKE2B-256"),
    BLAKE2B_384("BLAKE2B-384"),
    BLAKE2B_512("BLAKE2B-512");

    override fun toString(): String = algorithm
}