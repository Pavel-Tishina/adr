package com.paveltsikota.webcore.ffmpeg.dto

data class VideoIntervalsDto (
    val id: Long?,
    val sourceId: Long,
    val parts: MutableList<VideoPartsDto>,
)