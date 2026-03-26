package com.paveltsikota.webcore.utils.enums

enum class JobsType {
    DRAFT, // default value. Task type not set
    FILE_SCAN,
    MAKE_GROUPS,
    MAKE_HASHES,
    MOVE_DUPLICATES,
    BACK_DUPLICATES,
    RESTORE_DUPLICATES,
    DELETE_DUPLICATES,
    PURGE_DUPLICATES,
    SEARCH_IMAGE_DUPLICATES,
    PREPARE_VIDEO,
    SEARCH_VIDEO_DUPLICATES
}