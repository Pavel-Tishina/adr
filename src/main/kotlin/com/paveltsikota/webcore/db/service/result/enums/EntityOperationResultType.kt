package com.paveltsikota.webcore.db.service.result.enums

enum class EntityOperationResultType(private val isSingle: Boolean? = null) {
    ENTITY_ADD                  (true),
    ENTITY_NOT_ADD              (true),
    ENTITY_UPDATED              (true),
    ENTITY_NOT_UPDATED          (true),
    ENTITY_REMOVED              (true),
    ENTITY_NOT_REMOVED          (true),
    ENTITY_FOUND                (true),
    ENTITY_NOT_FOUND            (true),
    ENTITY_ALREADY_EXIST        (true),
    ENTITIES_ADDED              (false),
    ENTITIES_NOT_ADDED          (false),
    ENTITIES_ADDED_PARTLY       (false),
    ENTITIES_UPDATED            (false),
    ENTITIES_UPDATED_PARTLY     (false),
    ENTITIES_NOT_UPDATED        (false),
    ENTITIES_REMOVED            (false),
    ENTITIES_REMOVED_PARTLY     (false),
    ENTITIES_NOT_REMOVED        (false),
    ENTITIES_FOUNDED            (false),
    ENTITIES_FOUNDED_PARTLY     (false),
    ENTITIES_NOT_FOUNDED        (false),
    ENTITIES_ALREADY_EXISTED    (false),
    OK,
    ERROR;

    fun isSingle(): Boolean? {
        return isSingle
    }
}