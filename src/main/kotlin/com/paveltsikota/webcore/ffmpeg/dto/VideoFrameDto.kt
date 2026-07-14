package com.paveltsikota.webcore.ffmpeg.dto

import kotlin.time.Duration

data class VideoFrameDto (
    val id: Long,
    val videoPartsId: Long,
    val path: String,
    val duration: Duration,
    val partNumber: Int,
    val frameNumber: Int,
): FFMpegDto