package com.paveltsikota.webcore.utils.errors

object FileOperationErrors {
    val RESTORE_ERROR_WRONG_STATUS = "File not deleted by entity status"
    val RESTORE_ERROR_ORIGINAL_FILE_NOT_EXIST = "Original file not exist"
    val RESTORE_ERROR_RESTORED_FILE_ALREADY_EXIST = "Restored file already exist"
    val RESTORE_ERROR_SAME_PATH = "Restore and original path is same"

    val MOVE_TO_HASH_DIR_ERROR_WRONG_STATUS = "File not moved by entity status"
    val MOVE_TO_HASH_DIR_ERROR_FILE_NOT_EXIST = "Moved filed not exist"
    val MOVE_TO_HASH_DIR_ERROR_SAME_PATH = "Hash dir and source dir is the same"

    val ERROR_IO = "IO-error write/read of file"


}