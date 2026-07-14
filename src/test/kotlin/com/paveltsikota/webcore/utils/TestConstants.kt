package com.paveltsikota.webcore.utils

object TestConstants {
    val jsonVideoInfoOutput: String = """
        {
            "programs": [
        
            ],
            "stream_groups": [
        
            ],
            "streams": [
                {
                    "codec_name": "h264",
                    "codec_type": "video",
                    "width": 1280,
                    "height": 640,
                    "pix_fmt": "yuv420p",
                    "level": 31,
                    "color_range": "tv",
                    "bits_per_raw_sample": "8"
                },
                {
                    "codec_name": "ac3",
                    "codec_type": "audio"
                },
                {
                    "codec_name": "ac3",
                    "codec_type": "audio"
                },
                {
                    "codec_name": "ac3",
                    "codec_type": "audio"
                },
                {
                    "codec_name": "ac3",
                    "codec_type": "audio"
                },
                {
                    "codec_name": "ac3",
                    "codec_type": "audio"
                },
                {
                    "codec_name": "subrip",
                    "codec_type": "subtitle",
                    "duration_ts": 2852864
                },
                {
                    "codec_name": "subrip",
                    "codec_type": "subtitle",
                    "duration_ts": 2852864
                }
            ],
            "format": {
                "duration": "2852.864000",
                "size": "2239221469"
            }
        }
    """.trimIndent()

    val jsonVideoFrameDto: String = """
        {
            id: 1,
            videoPartsId: 123,
            path: "/path/to/my/file",
            duration: 2852.864000,
            intervalNumber: 0,
            partNumber: 0,
            frameNumber: 0,
        }
    """.trimIndent()

    val jsonVideoPartsDto: String = """
        {
            id: 123,
            intervalsId: 234,
            number: 345,
            frames: List<VideoFrameDto>
        }
    """.trimIndent()

}