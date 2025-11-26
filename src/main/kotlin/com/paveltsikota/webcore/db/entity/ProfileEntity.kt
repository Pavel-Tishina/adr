package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.db.convertor.MapToJsonConverter
import com.paveltsikota.webcore.db.utils.ConfigUtils.getDefaultConfigMap
import jakarta.persistence.*

@Entity
@Table(name = "profile")
data class ProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 64)
    var title: String = "",

    @Column(nullable = true, length = 256)
    var description: String = "",

    @Convert(converter = MapToJsonConverter::class)
    @Column(nullable = false, columnDefinition = "TEXT") // в Liquibase заменить на jsonb в PostgreSQL
    var cfg: Map<String, Any> = getDefaultConfigMap()

): CommonEntity {

    override fun same(o: Any?): Boolean {
        return o is ProfileEntity
                && title == o.title
                && description == o.description
                && cfg == o.cfg
    }

}