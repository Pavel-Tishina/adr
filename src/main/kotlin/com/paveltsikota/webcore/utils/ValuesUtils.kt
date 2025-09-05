package com.paveltsikota.webcore.utils

object ValuesUtils {

    fun validatePageParams(page: Int?, pageSize: Int?): Boolean {
        return page != null && pageSize != null && page > 0 && pageSize > 0
    }


}