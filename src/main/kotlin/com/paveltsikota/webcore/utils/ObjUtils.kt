package com.paveltsikota.webcore.utils

object ObjUtils {

    //ObjectUtils appache allNull / allNotNull?
    fun allIsNull(obj: Array<Any?>?): Boolean = obj == null || obj.all { it == null }

    fun allIsNotNull(obj: Array<Any?>?): Boolean = obj != null && obj.all { it != null }


}