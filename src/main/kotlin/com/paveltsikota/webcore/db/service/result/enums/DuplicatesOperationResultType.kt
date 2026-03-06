package com.paveltsikota.webcore.db.service.result.enums

enum class DuplicatesOperationResultType(private val isSingle: Boolean? = null) {
    OBJECT_ADD                  (true),
    OBJECT_NOT_ADD              (true),
    OBJECT_UPDATED              (true),
    OBJECT_NOT_UPDATED          (true),
    OBJECT_REMOVED              (true),
    OBJECT_NOT_REMOVED          (true),
    OBJECT_FOUND                (true),
    OBJECT_NOT_FOUND            (true),
    OBJECT_ALREADY_EXIST        (true),
    OBJECTS_ADDED              (false),
    OBJECTS_NOT_ADDED          (false),
    OBJECTS_ADDED_PARTLY       (false),
    OBJECTS_UPDATED            (false),
    OBJECTS_UPDATED_PARTLY     (false),
    OBJECTS_NOT_UPDATED        (false),
    OBJECTS_REMOVED            (false),
    OBJECTS_REMOVED_PARTLY     (false),
    OBJECTS_NOT_REMOVED        (false),
    OBJECTS_FOUNDED            (false),
    OBJECTS_FOUNDED_PARTLY     (false),
    OBJECTS_NOT_FOUNDED        (false),
    OBJECTS_ALREADY_EXISTED    (false),
    OK,
    ERROR;

    fun isSingle(): Boolean? {
        return isSingle
    }
}