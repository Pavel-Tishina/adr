package com.paveltsikota.webcore.utils.enums

import com.paveltsikota.webcore.utils.enums.HashTypeFamily.*
import com.paveltsikota.webcore.utils.enums.Blake2BType.*


enum class HashType(val family: HashTypeFamily, val algorithm: String) {
    MD2         (CRYPTO, "MD2"),
    MD5         (CRYPTO, "MD5"),
    SHA1        (CRYPTO, "SHA-1"),
    SHA256      (CRYPTO, "SHA-256"),
    SHA384      (CRYPTO, "SHA-384"),
    SHA512      (CRYPTO, "SHA-512"),
    XXHASH32    (XXHASH, "XXHASH32"),
    XXHASH64    (XXHASH, "XXHASH64"),
    XXHASH128   (XXHASH, "XXHASH128"),
    BLAKE2B160  (BLAKE, BLAKE2B_160.toString()),
    BLAKE2B256  (BLAKE, BLAKE2B_256.toString()),
    BLAKE2B384  (BLAKE, BLAKE2B_384.toString()),
    BLAKE2B512  (BLAKE, BLAKE2B_512.toString()),
    UNKNOWN     (ERROR, "")
}