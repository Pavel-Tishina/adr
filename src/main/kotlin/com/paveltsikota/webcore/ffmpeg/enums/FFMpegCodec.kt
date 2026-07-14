package com.paveltsikota.webcore.ffmpeg.enums

enum class FFMpegCodec(val codec: String, val param: String) {
    // Software
    H_264("libx264", "-sc_threshold 0"),
    H_265("libx265", "-x265-params \"no-scenecut=1:rc-lookahead=60\""),
    AV1("libaom-av1", ""),
//    AV1_SVT("libsvtav1", ""),
    VP9("libvpx-vp9", ""),
    MPEG_2("mpeg2video", ""),
    MPEG_4("mpeg4", ""),
//    FLV("flv1", ""),
//    PRORES("prores_ks", ""),
//    DNxHD("dnxhd", ""),

    // NVIDIA NVENC
//    H_264_NVIDIA("h264_nvenc", "-sc_threshold 0"),
//    H_265_NVIDIA("hevc_nvenc", "-x265-params \"no-scenecut=1:rc-lookahead=60\""),
//    AV1_NVIDIA("av1_nvenc", ""),

    // AMD AMF
//    H_264_AMD("h264_amf", "-sc_threshold 0"),
//    H_265_AMD("hevc_amf", "-x265-params \"no-scenecut=1:rc-lookahead=60\""),
//    AV1_AMD("av1_amf", ""),

    // Intel QSV
//    H_264_INTEL("h264_qsv", "-sc_threshold 0"),
//    H_265_INTEL("hevc_qsv", "-x265-params \"no-scenecut=1:rc-lookahead=60\""),
//    AV1_INTEL("av1_qsv", "")
}