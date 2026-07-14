package com.paveltsikota.webcore.ffmpeg.dto

import tools.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class FfProbeFormatDto(
    val duration: String,
    val size: String
): FFMpegDto