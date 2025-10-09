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

    @Suppress("UNCHECKED_CAST")
    fun <T> anyTo(value: Any?, clazz: Class<T>): T? {
        if (value == null) return null

        return when (clazz) {
            Long::class.java, java.lang.Long::class.java -> when (value) {
                is Int -> value.toLong() as T
                is Short -> value.toLong() as T
                is Byte -> value.toLong() as T
                is Long -> value as T
                else -> null
            }
            Int::class.java, Integer::class.java -> when (value) {
                is Long -> value.toInt() as T
                is Short -> value.toInt() as T
                is Byte -> value.toInt() as T
                is Int -> value as T
                else -> null
            }
            Double::class.java, java.lang.Double::class.java -> when (value) {
                is Number -> value.toDouble() as T
                else -> null
            }
            Float::class.java, java.lang.Float::class.java -> when (value) {
                is Number -> value.toFloat() as T
                else -> null
            }
            HashType::class.java -> when (value) {
                is HashType -> value as T
                is String -> HashType.valueOf(value) as T
                else -> null
            }
            else -> if (clazz.isInstance(value)) {
                value as T
            } else {
                null
            }
        }
    }

    fun profileIdChk(profileId: Long?): Boolean = profileId != null && profileId > 0
    fun priorityChk(priority: Int?): Boolean = priority != null && priority >= 0

}