package com.paveltsikota.webcore.ffmpeg.dto

import tools.jackson.module.kotlin.jacksonObjectMapper
import tools.jackson.module.kotlin.readValue

data class VideoInfoDto(
    val duration: Double,
    val size: Long,
    val codec: String,
    val width: Int,
    val height: Int,
    val pixelFormat: String?,
    val level: Short,
    val bitsPerRawSample: Byte,
    val colorRange: Int,
    val durationTs: String?
): FFMpegDto {
    companion object {
        fun from(json: String): VideoInfoDto {
            val ffprobe: FfProbeDto = jacksonObjectMapper().readValue(json)

            val video = ffprobe.streams.first {
                it.codec_type == "video"
            }

            return VideoInfoDto(
                duration = ffprobe.format.duration.toDouble(),
                size = ffprobe.format.size.toLong(),
                codec = video.codec_name,
                width = video.width ?: 0,
                height = video.height ?: 0,
                pixelFormat = video.pix_fmt,
                level = video.level ?: 0,
                bitsPerRawSample = video.bits_per_raw_sample ?: 0,
                colorRange = video.color_range ?: 0,
                durationTs = video.duration_ts
            )
        }
    }
}