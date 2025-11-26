package com.paveltsikota.webcore.utils

import com.paveltsikota.webcore.utils.enums.OsType

object OsUtils {
    const val OS_NAME_PROP = "os.name"
    const val WIN = "win"
    const val MAC = "mac"
    const val NIX = "nix"
    const val NUX = "nux"
    const val AIX = "aix"
    const val BSD = "bsd"


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