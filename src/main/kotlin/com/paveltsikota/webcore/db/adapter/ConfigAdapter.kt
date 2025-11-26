package com.paveltsikota.webcore.db.adapter

import com.paveltsikota.webcore.db.dto.CfgDto
import com.paveltsikota.webcore.db.entity.ConfigEntity
import org.springframework.stereotype.Component

@Component
object ConfigAdapter: AbstractEntityDtoAdapter<ConfigEntity, CfgDto>(
    entityClass = ConfigEntity::class,
    dtoClass = CfgDto::class
) {

    override fun entityToDto(e: ConfigEntity): CfgDto {
        return with(e) {
            CfgDto(hashDir, hashType, bufferSize, progressN, progressSize, progressShow, flyHashCalculate)
        }
    }

    override fun dtoToEntity(dto: CfgDto): ConfigEntity {
        return with(dto) {
            ConfigEntity(
                hashDir = hashDir,
                hashType = hashType,
                bufferSize = bufferSize,
                progressN = progressN,
                progressSize = progressSize, 
                progressShow = progressShow,
                flyHashCalculate = flyHashCalculate
            )
        }
    }

}