package com.paveltsikota.webcore.utils.entity

import com.paveltsikota.webcore.db.entity.ConfigEntity
import com.paveltsikota.webcore.db.dto.CfgDto
import com.paveltsikota.webcore.utils.FileUtils
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_BUFFER_SIZE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_FLY_HASH_CALCULATE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_HASH_DIR
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_HASH_TYPE
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_N
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_SHOW
import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_CFG_PROGRESS_SIZE
import com.paveltsikota.webcore.utils.enums.HashType
import kotlin.String
import kotlin.io.path.Path

object ConfigEntityUtils {

    fun eq(e1: ConfigEntity, e2: ConfigEntity): Boolean {
        return e1.id == e2.id
                && e1.progressShow && e2.progressShow
                && e1.flyHashCalculate && e2.flyHashCalculate
                && e1.profile == e2.profile
                && e1.bufferSize == e2.bufferSize
                && e1.progressN == e2.progressN
                && e1.progressSize == e2.progressSize
                && e1.hashDir == e2.hashDir
                && e1.hashType == e2.hashType
    }

    fun entityToDto(e: ConfigEntity = ConfigEntity()): CfgDto {
        return with(e) {
            CfgDto(
                id = id,
                hashDir = hashDir,
                hashType = hashType,
                bufferSize = bufferSize,
                progressN = progressN,
                progressSize = progressSize,
                progressShow = progressShow,
                flyHashCalculate = flyHashCalculate,
            )
        }
    }

    fun mapToDto(map: Map<String, Any> = getDefaultConfigMap()): CfgDto {
        return with(map) {
            CfgDto(
                id = get("id") as Long,
                hashDir = get("hashDir") as String,
                hashType = get("hashType") as HashType,
                bufferSize = get("bufferSize") as Long,
                progressN = get("progressN") as Int,
                progressSize = get("progressSize") as Long,
                progressShow = get("progressShow") as Boolean,
                flyHashCalculate = get("flyHashCalculate") as Boolean,
            )
        }
    }

    fun dtoToEntity(dto: CfgDto, profileId: Long = 0): ConfigEntity {
        return with(dto) {
            ConfigEntity(id = id,
                profile = profileId,
                hashDir = FileUtils.toUnixPath(Path(hashDir)),
                hashType = hashType,
                bufferSize = bufferSize,
                progressN = progressN,
                progressSize = progressSize,
                progressShow = progressShow,
                flyHashCalculate = flyHashCalculate,
                )
        }
    }

    fun dtoToMap(dto: CfgDto, profileId: Long = 0): Map<String, Any> {
        return with(dto) {
            mapOf("id" to id,
                "profile" to profileId,
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
            Pair("id", 0),
            Pair("profile", 0),
            Pair("hashDir", DEFAULT_CFG_HASH_DIR),
            Pair("hashType", DEFAULT_CFG_HASH_TYPE),
            Pair("bufferSize", DEFAULT_CFG_BUFFER_SIZE),
            Pair("progressN", DEFAULT_CFG_PROGRESS_N),
            Pair("progressSize", DEFAULT_CFG_PROGRESS_SIZE),
            Pair("progressShow", DEFAULT_CFG_PROGRESS_SHOW),
            Pair("flyHashCalculate", DEFAULT_CFG_FLY_HASH_CALCULATE)
        )
    }

}