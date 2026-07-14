package com.paveltsikota.webcore.ffmpeg.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class FfProbeDto(
    val streams: List<FfProbeStreamDto>,
    val format: FfProbeFormatDto
): FFMpegDto