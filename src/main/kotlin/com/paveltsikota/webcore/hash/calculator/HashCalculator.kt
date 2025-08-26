package com.paveltsikota.webcore.hash.calculator

import com.paveltsikota.webcore.utils.enums.HashType
import java.io.InputStream
import java.nio.file.Path

interface HashCalculator {

    fun calculate(path: Path): String
    fun calculate(stream: InputStream): String
    fun getType(): HashType
}