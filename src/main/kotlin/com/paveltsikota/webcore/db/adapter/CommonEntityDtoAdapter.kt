package com.paveltsikota.webcore.db.adapter

import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class CommonEntityDtoAdapter(
    adapters: List<AbstractEntityDtoAdapter<*, *>>
) {
//    private val entityToAdapter: Map<KClass<*>, AbstractEntityDtoAdapter<*, *>> =
//        adapters.associateBy<AbstractEntityDtoAdapter<*, *>, KClass<*>> { it.entityClass as KClass<*> }
//
//    private val dtoToAdapter: Map<KClass<*>, AbstractEntityDtoAdapter<*, *>> =
//        adapters.associateBy<AbstractEntityDtoAdapter<*, *>, KClass<*>> { it.dtoClass as KClass<*> }

    private val entityToAdapter: Map<KClass<*>, AbstractEntityDtoAdapter<*, *>> =
        adapters.associateBy { it.entityClass }

    private val dtoToAdapter: Map<KClass<*>, AbstractEntityDtoAdapter<*, *>> =
        adapters.associateBy { it.dtoClass }

    @Suppress("UNCHECKED_CAST")
    fun <E : Any, D : Any> eqEntity(e1: E, e2: E): Boolean? {
        val adapter = entityToAdapter[e1::class] as? AbstractEntityDtoAdapter<E, D> ?: return null
        return adapter.eqEntity(e1, e2)
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Any, D : Any> eqDto(dto1: D, dto2: D): Boolean? {
        val adapter = dtoToAdapter[dto1::class] as? AbstractEntityDtoAdapter<E, D> ?: return null
        return adapter.eqDto(dto1, dto2)
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Any, D : Any> dtoToEntity(dto: D): E? {
        val adapter = entityToAdapter[dto::class] as? AbstractEntityDtoAdapter<E, D> ?: return null
        return adapter.dtoToEntity(dto)
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Any, D : Any> entityToDto(e: E): D? {
        val adapter = dtoToAdapter[e::class] as? AbstractEntityDtoAdapter<E, D> ?: return null
        return adapter.entityToDto(e)
    }
}