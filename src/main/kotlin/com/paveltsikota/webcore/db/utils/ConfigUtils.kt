package com.paveltsikota.webcore.db.utils

import com.paveltsikota.webcore.db.dto.CfgDto
import com.paveltsikota.webcore.utils.FileUtils
import com.paveltsikota.webcore.utils.ValuesUtils.anyTo
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_BUFFER_SIZE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_FLY_HASH_CALCULATE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_HASH_DIR
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_HASH_TYPE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_N
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_SHOW
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_SIZE
import com.paveltsikota.webcore.utils.enums.HashType
import kotlin.io.path.Path

object ConfigUtils {

    fun mapToDto(map: Map<String, Any> = getDefaultConfigMap()): CfgDto {
        return with(map) {
            CfgDto(
                hashDir = get("hashDir") as String,
                hashType = HashType.valueOf(get("hashType").toString()),
                bufferSize = anyTo(get("bufferSize"), Long::class.java)?: DEFAULT_CFG_BUFFER_SIZE,
                progressN = anyTo(get("progressN"), Int::class.java)?: DEFAULT_CFG_PROGRESS_N,
                progressSize = anyTo(get("progressSize"), Long::class.java)?: DEFAULT_CFG_PROGRESS_SIZE,
                progressShow = get("progressShow") as Boolean,
                flyHashCalculate = get("flyHashCalculate") as Boolean,
            )
        }
    }

    fun dtoToMap(dto: CfgDto): Map<String, Any> {
        return with(dto) {
            mapOf(
                "hashDir" to FileUtils.toUnixPath(Path(hashDir)),
                "hashType" to hashType,
                "bufferSize" to bufferSize,
                "progressN" to progressN,
                "progressSize" to progressSize,
                "progressShow" to progressShow,
                "flyHashCalculate" to flyHashCalculate,
            )
        }
    }

    fun getDefaultConfigMap(): Map<String, Any> {
        return mapOf(
            "hashDir" to DEFAULT_CFG_HASH_DIR,
            "hashType" to DEFAULT_CFG_HASH_TYPE,
            "bufferSize" to DEFAULT_CFG_BUFFER_SIZE,
            "progressN" to DEFAULT_CFG_PROGRESS_N,
            "progressSize" to DEFAULT_CFG_PROGRESS_SIZE,
            "progressShow" to DEFAULT_CFG_PROGRESS_SHOW,
            "flyHashCalculate" to DEFAULT_CFG_FLY_HASH_CALCULATE
        )
    }

}
