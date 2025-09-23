package com.paveltsikota.webcore.utils

import com.paveltsikota.webcore.utils.enums.HashType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object ValuesUtils {

    fun validatePageParams(page: Int?, pageSize: Int?): Boolean {
        return page != null && pageSize != null && page > 0 && pageSize > 0
    }

    fun validateFindByHash(hash: String?, hashType: HashType?, profileId: Long?): Boolean {
        return hash != null
                && hash.isNotBlank()
                && hashType != null
                && hashType != HashType.UNKNOWN
                && profileId != null
                && profileId > 0
    }

    inline fun <reified T : Any> T.toMap(): Map<String, Any?> {
        return T::class.memberProperties.associate { prop ->
            prop.name to prop.get(this)
        }
    }

    inline fun <reified T : Any> Map<String, Any?>.toDto(): T {
        val ctor = T::class.primaryConstructor
            ?: throw IllegalArgumentException("Class must have primary constructor")

        val args = ctor.parameters.associateWith { param ->
            this[param.name]
        }

        return ctor.callBy(args)
    }

}