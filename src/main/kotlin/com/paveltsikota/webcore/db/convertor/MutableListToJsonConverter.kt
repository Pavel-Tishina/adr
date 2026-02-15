package com.paveltsikota.webcore.db.convertor

import jakarta.persistence.Converter

@Converter(autoApply = true)
class MutableListToJsonConverter : AbstractMutableCollectionToJsonConvertor<Long>(Long::class.java)