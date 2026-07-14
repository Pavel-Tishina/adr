package com.paveltsikota.webcore.ffmpeg.dto

data class VideoPartsDto (
    val id: Long?,
    val intervalsId: Long?,
    val number: Int,
    val frames: MutableList<VideoFrameDto>
): FFMpegDto