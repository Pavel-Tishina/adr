package com.paveltsikota.webcore.db.adapter

abstract class AbstractEntityDtoAdapter<E, D>(
    internal val entityClass: Class<E>,
    internal val dtoClass: Class<D>
) {
    abstract fun eqEntity(e1: E, e2: E): Boolean
    abstract fun eqDto(dto1: D, dto2: D): Boolean
    abstract fun entityToDto(e: E): D
    abstract fun dtoToEntity(dto: D): E
}