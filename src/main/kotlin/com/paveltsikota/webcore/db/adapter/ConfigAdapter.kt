package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.CfgDto
import com.paveltsikota.webcore.db.entity.ConfigEntity
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
import org.springframework.stereotype.Component
import kotlin.io.path.Path

@Component
object ConfigAdapter: AbstractEntityDtoAdapter<ConfigEntity, CfgDto>(
    entityClass = ConfigEntity::class.java,
    dtoClass = CfgDto::class.java
) {

    override fun eqEntity(e1: ConfigEntity, e2: ConfigEntity): Boolean {
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

    override fun eqDto(dto1: CfgDto, dto2: CfgDto): Boolean {
        return dto1 == dto2
    }

    override fun entityToDto(e: ConfigEntity): CfgDto {
        return with(e) {
            CfgDto(
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

    override fun dtoToEntity(dto: CfgDto): ConfigEntity {
        TODO("Not yet implemented")
    }

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

    fun dtoToEntity(dto: CfgDto, profileId: Long = 0): ConfigEntity {
        return with(dto) {
            ConfigEntity(
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