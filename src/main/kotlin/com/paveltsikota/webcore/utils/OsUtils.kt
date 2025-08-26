package com.paveltsikota.webcore.utils

import com.paveltsikota.webcore.utils.enums.OsType

object OsUtils {
    val OS_NAME_PROP = "os.name"
    val WIN = "win"
    val MAC = "mac"
    val NIX = "nix"
    val NUX = "nux"
    val AIX = "aix"
    val BSD = "bsd"


    fun detectOS(): OsType {
        val os = System.getProperty(OS_NAME_PROP).lowercase()
        return when {
            os.contains(WIN, ignoreCase = true) -> OsType.WINDOWS
            os.contains(MAC, ignoreCase = true) -> OsType.MACOS
            os.contains(NIX, ignoreCase = true)
                    || os.contains(NUX, ignoreCase = true)
                    || os.contains(AIX, ignoreCase = true) -> OsType.LINUX
            os.contains(BSD, ignoreCase = true) -> OsType.BSD
            else -> OsType.UNKNOWN
        }
    }

}