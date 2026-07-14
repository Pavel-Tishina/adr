package com.paveltsikota.webcore.db.type

typealias QueryOrderMap = LinkedHashMap<String, EntityOrder?>
typealias QueryParamMap = LinkedHashMap<String, Any>

fun QueryOrderMap.toSqlQuery(): String =
    if (entries.isEmpty()) "" else " ORDER BY " + entries.joinToString{ (k, v) -> v?.let{"$k $it"}?: k }

fun QueryParamMap.toSqlQuery(e: String): String =
    if (keys.isEmpty()) "" else " WHERE " + keys.joinToString(separator = " AND ") { "$e.$it = :$it" }
