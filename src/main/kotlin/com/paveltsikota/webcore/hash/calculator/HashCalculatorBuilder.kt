package com.paveltsikota.webcore.hash.calculator

import com.paveltsikota.webcore.hash.calculator.impl.Blake2B
import com.paveltsikota.webcore.hash.calculator.impl.CryptoHash
import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import com.paveltsikota.webcore.utils.enums.Blake2BType
import com.paveltsikota.webcore.utils.enums.CryptoHashType
import com.paveltsikota.webcore.utils.enums.HashType
import com.paveltsikota.webcore.utils.enums.HashTypeFamily.*

class HashCalculatorBuilder {

    fun build(hashType: HashType): HashCalculator? = when(hashType.family) {
        XXHASH -> XXHash64
        CRYPTO -> CryptoHash(CryptoHashType.valueOf(hashType.algorithm))
        BLAKE -> Blake2B(Blake2BType.valueOf(hashType.algorithm))
        else -> null
    }

    fun saveBuild(hashType: HashType): HashCalculator = build(hashType)?: XXHash64

}