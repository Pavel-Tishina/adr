package com.paveltsikota.webcore.ffmpeg.dto

import tools.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class FfProbeStreamDto(
    val codec_name: String,
    val codec_type: String,
    val width: Int? = null,
    val height: Int? = null,
    val pix_fmt: String? = null,
    val level: Short? = null,
    val bits_per_raw_sample: Byte? = null,
    val color_range: Int? = null,
    val duration_ts: String? = null
): FFMpegDto