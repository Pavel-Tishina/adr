package com.paveltsikota.webcore.db.service.result

import com.paveltsikota.webcore.db.service.result.enums.ResultObjectSetType
import com.paveltsikota.webcore.db.service.result.enums.ResultObjectSetType.*

class ResultObjects<T: Any>{

    private val mapSet = mapOf<ResultObjectSetType, LinkedHashSet<T>>(
        GOOD to LinkedHashSet(),
        WARN to LinkedHashSet(),
        ERROR to LinkedHashSet(),
    )

    fun add(e: T, t: ResultObjectSetType): Boolean = mapSet[t]?.add(e) ?: false
    fun addAll(e: Collection<T>, t: ResultObjectSetType): Boolean = mapSet[t]?.addAll(e) ?: false

    fun remove(e: T, t: ResultObjectSetType): Boolean =  mapSet[t]?.remove(e) ?: false
    fun removeAll(e: Collection<T>, t: ResultObjectSetType): Boolean = mapSet[t]?.removeAll(e) ?:false

    fun contains(e: T, t: ResultObjectSetType): Boolean = mapSet[t]?.contains(e) ?: false
    fun containsAll(e: Collection<T>, t: ResultObjectSetType): Boolean = mapSet[t]?.containsAll(e) ?: false

    fun isNotEmpty(t: ResultObjectSetType): Boolean = mapSet[t].notNullOrEmpty()

    fun getList(t: ResultObjectSetType): List<T> = mapSet[t]?.toList() ?: emptyList()
    fun getNullIfEmpty(t: ResultObjectSetType): List<T>? = mapSet[t].takeIf { it.notNullOrEmpty() }?.toList()

    fun clear(t: ResultObjectSetType): Unit = mapSet[t]?.clear() ?: Unit

    fun isSuccess(): Boolean = mapSet[GOOD].notNullOrEmpty()
    fun isBad(): Boolean = mapSet[GOOD].isNullOrEmpty() && mapSet[ERROR].notNullOrEmpty()
    fun hasProblems(): Boolean = isSuccess() && (mapSet[ERROR].notNullOrEmpty() || mapSet[WARN].notNullOrEmpty())

    private fun <T> Collection<T>?.notNullOrEmpty(): Boolean = !this.isNullOrEmpty()
}