package com.paveltsikota.webcore.db.adapter

import kotlin.reflect.KClass

abstract class AbstractEntityDtoAdapter<E : Any, D : Any>(
    internal val entityClass: KClass<E>,
    internal val dtoClass: KClass<D>
) {
    abstract fun entityToDto(e: E): D
    abstract fun dtoToEntity(dto: D): E
}