package com.paveltsikota.webcore.utils


import com.paveltsikota.webcore.ffmpeg.FFMpegCliUtils.buildShowVideoErrorsCmd
import com.paveltsikota.webcore.ffmpeg.FFMpegCliUtils.buildVideoHashFileCmd
import com.paveltsikota.webcore.ffmpeg.FFMpegCliUtils.buildVideoInfoCmd
import com.paveltsikota.webcore.ffmpeg.FFMpegCliUtils.runCommand
import com.paveltsikota.webcore.ffmpeg.dto.VideoInfoDto
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.DurationUnit
import kotlin.time.toDuration


object FFMpegCliUtilsTest {
    val source = Path.of("E:\\films\\original\\Mulan.1998.BDRip-AVC_andre90.mkv")
    val dest = "E:\\films\\"


    @Test
    fun `getFFMpegCommand test`() {
//        assertEquals("", getFFMpegCommand(
//            source, Path.of(dest, "compress_${Clock.System.now().epochSeconds}.mp4"), WINDOWS))
//

//        val cmd = buildVideoHashFileCmd(
//            source, Path.of(dest, "compress_${Clock.System.now().epochSeconds}.mp4"))

        val cmd = buildShowVideoErrorsCmd(source)
        val cmd2 = buildVideoInfoCmd(source)

            // CHK CMD
//        val cmd = buildVideoInfoCmd(source)
//        assertEquals("", runCommand(cmd))

        val videoInfoDto1 = VideoInfoDto(
            duration = 123.123,
            size = 21321,
            codec = "test",
            width = 0,
            height = 0,
            pixelFormat = "??",
            level = 666,
            bitsPerRawSample = 2,
            colorRange = 2,
            durationTs = ""
        )

        assertEquals(videoInfoDto1, VideoInfoDto.from(runCommand(cmd2)))

//        assertEquals("", timeToCmd(453400.toDuration(DurationUnit.MILLISECONDS)))
    }
//
//    fun runCommand(command: String, os: OsType): String {
//        return ""
//    }

}