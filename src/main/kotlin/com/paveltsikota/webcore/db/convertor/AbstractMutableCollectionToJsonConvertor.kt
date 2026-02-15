package com.paveltsikota.webcore.db.convertor

import jakarta.persistence.AttributeConverter
import tools.jackson.databind.type.CollectionType
import tools.jackson.module.kotlin.jacksonObjectMapper

abstract class AbstractMutableCollectionToJsonConvertor<T>(
    private val elementClass: Class<T>
) : AttributeConverter<MutableCollection<T>?, String?> {

    private val objectMapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: MutableCollection<T>?): String? {
        return attribute?.let { objectMapper.writeValueAsString(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): MutableCollection<T>? {
        if (dbData == null) return null

        val type: CollectionType? = objectMapper
            .typeFactory
            .constructCollectionType(MutableCollection::class.java, elementClass)

        return objectMapper.readValue(dbData, type)
    }
}
