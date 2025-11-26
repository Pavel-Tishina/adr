package com.paveltsikota.webcore.db.utils

import com.paveltsikota.webcore.db.entity.HashesEntity

object HashesUtils {

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