package com.paveltsikota.webcore.ffmpeg

import com.paveltsikota.webcore.ffmpeg.dto.VideoFrameDto
import com.paveltsikota.webcore.ffmpeg.dto.VideoInfoDto
import com.paveltsikota.webcore.ffmpeg.dto.VideoIntervalsDto
import com.paveltsikota.webcore.ffmpeg.dto.VideoPartsDto
import com.paveltsikota.webcore.utils.FileUtils
import com.paveltsikota.webcore.utils.OsUtils
import com.paveltsikota.webcore.ffmpeg.enums.FFMpegCodec
import com.paveltsikota.webcore.ffmpeg.enums.FFMpegPreset
import com.paveltsikota.webcore.utils.enums.OsType
import com.paveltsikota.webcore.ffmpeg.exceptions.EmptyFFMpegCommandException
import com.paveltsikota.webcore.ffmpeg.exceptions.NoSourceFFMpegCommandException
import com.paveltsikota.webcore.ffmpeg.exceptions.UnknownSystemFFMpegCommandException
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object FFMpegCliUtils {
    private val frameSizePercentage = 10
    private val frameRatio = "4:3"

    private val pixFormat = "yuv420p"

    private val fps = 0.15
    private val crf = 36

    private val noAudio = true
    private val grayscale = true

//    private val frameMinHeight = 64
    private val frameMinHeight = 56
//    private val frameMinHeight = 48
    private val frameMaxHeight = 80

//    private val frameMinWidth = 128
    private val frameMinWidth = 112
//    private val frameMinWidth = 96
    private val frameMaxWidth = 160

    private val g = 9999
    private val keyInt = 9999


    private val preset = FFMpegPreset.VERY_SLOW
    private val codec = FFMpegCodec.H_264

    private val OS = OsUtils.detectOS()
    private val Q = if (OS == OsType.WINDOWS) { "\"" } else { "'"}
    private val FILTER = when (OS) {
        OsType.UNIX, OsType.LINUX, OsType.MACOS -> "grep"
        OsType.WINDOWS -> "find"
        else -> ""
    }


    fun buildVideoHashFileCmd(source: Path, dest: Path): String =
        if (allOk(source))
            "ffmpeg -nostdin -i " +
                    "${Q}${FileUtils.toUnixPath(source)}${Q} " +
                    "-vf ${Q}scale=$frameMinWidth:$frameMinHeight,fps=$fps,format=gray${Q} " +
                    "-c:v ${codec.codec} " +
                    "-preset ${preset.v} " +
                    "-pix_fmt  $pixFormat " +
                    "-crf $crf " +
                    ("${codec.param} ".takeIf { codec.param.isNotEmpty() } ?: "") +
                    "-g $g " +
                    "-keyint_min $keyInt " +
                    "-an " +
                    "${Q}${FileUtils.toUnixPath(dest)}${Q}"
        else
            throw if (OS == OsType.UNKNOWN) UnknownSystemFFMpegCommandException() else NoSourceFFMpegCommandException()

    fun buildVideoInfoCmd(source: Path): String =
        if (allOk(source))
            "ffprobe -v quiet -print_format json " +
                    "-show_entries " +
                    "${Q}stream=codec_name,codec_type,width,height,pix_fmt,level,bits_per_raw_sample,color_range,duration_ts" +
                    ":format=duration,size${Q} " +
                    "${Q}${FileUtils.toUnixPath(source)}${Q}"
        else
            throw if (OS == OsType.UNKNOWN) UnknownSystemFFMpegCommandException() else NoSourceFFMpegCommandException()

    fun buildShowVideoErrorsCmd(source: Path): String =
        if (allOk(source))
            "ffmpeg -v error -i ${Q}${FileUtils.toUnixPath(source)}${Q} -f null -"
        else
            throw if (OS == OsType.UNKNOWN) UnknownSystemFFMpegCommandException() else NoSourceFFMpegCommandException()

    fun buildShowVideoErrorsDetailedCmd(source: Path, videoStreams: Set<Int>): String =
        if (allOk(source))
            "ffprobe -v error " +
                    "-select_streams v:${videoStreams.joinToString { "," }} " +
                    "-show_entries frame=pts_time,pkt_dts_time,pict_type -show_frames " +
                    "-of csv=p=0 " + //csv variant
//                  "-of json " + //json variant, pretty print brake idea with grep/find
                    "${Q}${FileUtils.toUnixPath(source)}${Q} " +
                    "| $FILTER \"N/A\""
        else
            throw if (OS == OsType.UNKNOWN) UnknownSystemFFMpegCommandException() else NoSourceFFMpegCommandException()

    fun buildShowVideoBadFramesCmd(source: Path): String =
        if (allOk(source))
            "ffmpeg -v warning -i ${Q}${FileUtils.toUnixPath(source)}${Q} -vf showinfo -f null - 2>&1 | $FILTER \"showinfo\\|error\""
        else
            throw if (OS == OsType.UNKNOWN) UnknownSystemFFMpegCommandException() else NoSourceFFMpegCommandException()

    fun buildRecoverVideoFileCmd(broken: Path, recover: Path): String =
        if (allOk(broken))
            "ffmpeg -i " +
                "${Q}${FileUtils.toUnixPath(broken)}${Q} " +
                "-c copy " +
                "${Q}${FileUtils.toUnixPath(recover)}${Q}"
        else
            throw if (OS == OsType.UNKNOWN) UnknownSystemFFMpegCommandException() else NoSourceFFMpegCommandException()

    fun buildExtractFrameCmd(source: Path, frame: Path, time: Duration): String =
        if (allOk(source))
            "ffmpeg -ss ${time.toDouble(DurationUnit.SECONDS)} -i " +
                    "${Q}${FileUtils.toUnixPath(source)}${Q} " +
                    "-frames:v 1 " +
                    "${Q}${FileUtils.toUnixPath(frame)}${Q}"
        else
            throw if (OS == OsType.UNKNOWN) UnknownSystemFFMpegCommandException() else NoSourceFFMpegCommandException()


    fun runCommand(command: String): String {
        if (command.isBlank()) throw EmptyFFMpegCommandException()

        val process = when (OS) {
            OsType.WINDOWS -> ProcessBuilder("cmd", "/c", command)
            OsType.UNIX, OsType.LINUX, OsType.MACOS -> ProcessBuilder("sh", "-c", command)
            else -> throw UnknownSystemFFMpegCommandException()
        }.redirectErrorStream(true).start()

        process.outputStream.close()

        val output = process.inputStream.bufferedReader().use { it.readText() }

        return output
    }

    fun getVideoInfo(source: Path): VideoInfoDto =
        runCommand(buildVideoInfoCmd(source)).let { VideoInfoDto.from(it) }

    fun extractFrames(sourceFile: Path, destDir: String, frameNamePrefix: String, sourceId: Long, timeList: List<List<Duration>>): VideoIntervalsDto {
        val outDto = VideoIntervalsDto(id = -1, sourceId = sourceId, parts = ArrayList())
        timeList.forEachIndexed  { partInx, parts ->
            run {
                val partDto =
                    VideoPartsDto(id = -1, intervalsId = -1, number = partInx, frames = ArrayList())

                parts.forEachIndexed { frameInx, frameTime ->
                    run {
                        val frameFile: Path = Path.of(destDir, "${frameNamePrefix}_${partInx}_${frameInx}.png")
                        try {
                            val extractionResult = runCommand(buildExtractFrameCmd(sourceFile, frameFile, frameTime))
                            val frame = VideoFrameDto(
                                id = -1,
                                videoPartsId = -1,
                                path = FileUtils.toUnixPath(frameFile),
                                duration = 12313123.toDuration(DurationUnit.MILLISECONDS),
                                partNumber = partInx,
                                frameNumber = frameInx,
                            )
                            partDto.frames.add(frame)
                        } catch (e: Throwable) {
                        }
                    }
                }
                outDto.parts.add(partDto)
            }
        }

        return outDto
    }

    private fun allOk(source: Path): Boolean = OS != OsType.UNKNOWN && source.exists()


}