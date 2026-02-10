package com.paveltsikota.webcore.db.convertor


import tools.jackson.core.type.TypeReference
import tools.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class MapToJsonConverter : AttributeConverter<Map<String, Any>, String> {
    private val mapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: Map<String, Any>?): String {
        return mapper.writeValueAsString(attribute ?: emptyMap<String, Any>())
    }

    override fun convertToEntityAttribute(dbData: String?): Map<String, Any> {
        if (dbData.isNullOrBlank()) return emptyMap()
        return mapper.readValue(dbData, object : TypeReference<Map<String, Any>>() {})
    }
}