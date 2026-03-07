package com.paveltsikota.webcore.db.entity

import com.paveltsikota.webcore.utils.constant.Constants.DEFAULT_PROFILE
import com.paveltsikota.webcore.utils.enums.HashType
import jakarta.persistence.*

@Entity
@Table(name = "hashes")
data class HashesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val profile: Long = DEFAULT_PROFILE,

    @Column(nullable = false)
    val size: Long = 0,

    @Column(nullable = false)
    val hash: String = "",

    @Column(name = "hashType", nullable = false)
    val hashType: HashType = HashType.UNKNOWN,

    @Column(nullable = false)
    var main: Long = 0,

    @Column(nullable = false)
    var duplicates: MutableSet<Long> = hashSetOf(),

    @Column(name = "jobId", nullable = true)
    val jobId: Long? = null,

    ): CommonEntity {

    override fun same(o: Any?): Boolean {
        return o is HashesEntity
                && profile == o.profile
                && size == o.size
                && jobId == o.jobId
                && main == o.main
                && hashType == o.hashType
                && hash == o.hash
                && duplicates == o.duplicates
    }

}