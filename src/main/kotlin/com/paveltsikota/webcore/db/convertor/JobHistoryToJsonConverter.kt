package com.paveltsikota.webcore.db.convertor

import com.paveltsikota.webcore.db.dto.HistoryElementDto
import jakarta.persistence.Converter

@Converter(autoApply = true)
class JobHistoryToJsonConverter :
    AbstractMutableCollectionToJsonConvertor<HistoryElementDto>(HistoryElementDto::class.java)