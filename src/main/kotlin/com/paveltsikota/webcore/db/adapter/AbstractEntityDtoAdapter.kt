package com.paveltsikota.webcore.db.adapter

import kotlin.reflect.KClass

abstract class AbstractEntityDtoAdapter<E : Any, D : Any>(
    internal val entityClass: KClass<E>,
    internal val dtoClass: KClass<D>
) {
    abstract fun eqEntity(e1: E, e2: E): Boolean
    abstract fun eqDto(dto1: D, dto2: D): Boolean
    abstract fun entityToDto(e: E): D
    abstract fun dtoToEntity(dto: D): E
}