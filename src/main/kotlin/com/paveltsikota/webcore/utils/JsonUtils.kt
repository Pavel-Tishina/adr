package com.paveltsikota.webcore.utils

import com.paveltsikota.webcore.ffmpeg.dto.VideoFrameDto
import com.paveltsikota.webcore.ffmpeg.dto.VideoPartsDto
import tools.jackson.module.kotlin.jacksonObjectMapper
import tools.jackson.module.kotlin.readValue

object JsonUtils {
    val mapper = jacksonObjectMapper()

    fun toVideoFrameDto(json: String): VideoFrameDto = jsonTo(json)

    fun toVideoPartsDto(json: String): VideoPartsDto = jsonTo(json)

    inline fun <reified T> jsonTo(json: String): T = mapper.readValue(json)

}