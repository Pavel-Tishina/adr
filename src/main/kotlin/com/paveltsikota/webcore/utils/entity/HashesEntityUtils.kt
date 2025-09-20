package com.paveltsikota.webcore.utils.entity

import com.paveltsikota.webcore.db.entity.HashesEntity
import com.paveltsikota.webcore.db.dto.HashesDto

object HashesEntityUtils {

    fun eq(e1: HashesEntity, e2: HashesEntity): Boolean {
        return e1.id == e2.id
                && e1.size == e2.size
                && e1.profile == e2.profile
                && e1.main == e2.main
                && e1.hash == e2.hash
                && e1.hashType == e2.hashType
                && e1.duplicates == e2.duplicates
    }

    fun entityToDto(e: HashesEntity): HashesDto {
        return with(e) {
            HashesDto(
                id = id,
                size = size,
                profile = profile,
                hash = hash,
                hashType = hashType,
                main = main,
                duplicates = duplicates
            )
        }
    }

    fun dtoToEntity(dto: HashesDto): HashesEntity {
        return with(dto) {
            HashesEntity(
                id = id ?: 0,
                profile = profile,
                size = size,
                hash = hash,
                hashType = hashType,
                main = main,
                duplicates = (duplicates as MutableSet<Long>)
            )
        }
    }

    fun addDuplicate(e: HashesEntity, dupId: Long): HashesEntity {
        if (e.main != dupId) e.duplicates.add(dupId)
        return e
    }

    fun addDuplicates(e: HashesEntity, addDuplicates: Collection<Long>): HashesEntity {
        e.duplicates.addAll(addDuplicates.filter { it != e.main })
        return e
    }

    fun delDuplicate(e: HashesEntity, dupId: Long): HashesEntity {
        if (e.main == dupId) {
            e.main = e.duplicates.first()
            e.duplicates.remove(e.main)
        } else {
            e.duplicates.remove(dupId)
        }

        return e
    }

    fun delDuplicates(e: HashesEntity, delDuplicates: Collection<Long>): HashesEntity {
        e.duplicates.removeAll(delDuplicates)

        if (delDuplicates.contains(e.main)) {
            e.main = e.duplicates.first()
            e.duplicates.remove(e.main)
        }

        return e
    }

    fun setMain(e: HashesEntity, main: Long): HashesEntity {
        e.duplicates.remove(main)
        e.duplicates.add(e.main)
        e.main = main
        return e
    }

    fun setUpdate(e: HashesEntity, main: Long, dups: Collection<Long>): HashesEntity {
        e.duplicates = dups.toMutableSet()
        e.duplicates.add(e.main)
        e.main = main
        return e
    }

}