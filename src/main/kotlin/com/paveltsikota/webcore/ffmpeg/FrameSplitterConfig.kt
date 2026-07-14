package com.paveltsikota.webcore.ffmpeg

import com.paveltsikota.webcore.ffmpeg.FFMpegCliUtils.getVideoInfo
import com.paveltsikota.webcore.ffmpeg.dto.VideoFrameDto
import com.paveltsikota.webcore.ffmpeg.dto.VideoPartsDto
import java.nio.file.Path
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.times
import kotlin.time.toDuration

class FrameSplitterConfig (
    val intervalParts: Int = 5,
    val stepFromStart: Double = 2.5,
    val stepFromEnd: Double = 2.5,

    val partsCount: Int = 10,
    val partsTimeRange: Duration = 1L.toDuration(DurationUnit.SECONDS),
    val partsTimeInc: Duration = 750L.toDuration(DurationUnit.MILLISECONDS),
    ) {

    fun calculateTimes(source: Path): List<List<Duration>> {
        val videoInfo = getVideoInfo(source)

        val startTime = percentOfDuration(videoInfo.duration, stepFromStart)
        val endTime = videoInfo.duration.toDuration(DurationUnit.SECONDS)
            .minus(percentOfDuration(videoInfo.duration, stepFromEnd))

        val intervalStep = (endTime - startTime) / 4
        val intervals = (0 until intervalParts).map { i -> startTime + i * intervalStep }

        val durations: List<Duration> = (-partsCount..partsCount).map { partsTimeRange + it * partsTimeInc }

        return intervals.map { interval -> durations.map { offset -> interval + offset } }
    }

    fun percentOfDuration(duration: Double, percent: Double): Duration = (duration / 100 * percent).toDuration(DurationUnit.SECONDS)


}