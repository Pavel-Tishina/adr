package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.dto.HashesDto
import com.paveltsikota.webcore.db.service.HashesService
import com.paveltsikota.webcore.rest.model.HashesResponse
import com.paveltsikota.webcore.rest.model.PostGetManyHashesRequest
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getHashesResponse
import com.paveltsikota.webcore.utils.ValuesUtils.validateFindByHash
import com.paveltsikota.webcore.utils.enums.HashType
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/rest/v1/test/db/hashes")
class TestDbHashesController(private val hashesService: HashesService) {

    @GetMapping("/{id}")
    fun getHashById(@PathVariable id: Long): HashesResponse {
        val opResult = hashesService.getById(id)

        return getHashesResponse(opResult)
    }

    // Dirty... too dirty... but it's just for test repository!
    @GetMapping("/")
    fun getHash(
        @RequestParam(required = false) profileId: Long?,
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?,
        @RequestParam(required = false) n: Int?,
        @RequestParam(required = false) size: Long?,
        @RequestParam(required = false) hash: String?,
        @RequestParam(required = false) hashType: HashType?,
    ): HashesResponse {
        val opResult = when {
            validateFindByHash(hash, hashType, profileId)
                -> hashesService.findByHash(hash!!, hashType!!, profileId!!)
            size != null && profileId != null
                -> hashesService.findBySize(page, pageSize, size, profileId)
            else
                -> hashesService.getAllByN(page, pageSize, profileId, n)
        }

        return getHashesResponse(opResult)
    }

    @PostMapping("/many")
    fun getManyHashes(@RequestBody model: PostGetManyHashesRequest): HashesResponse {
        val opResult = hashesService.getByIds(model.ids)

        return getHashesResponse(opResult)
    }

    @PostMapping("/")
    fun addHash(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestParam model: HashesDto
    ): HashesResponse {
        val opResult = with(model) {
            hashesService.add(profile, size, hash, hashType, main, duplicates, addOnce)
        }

        return getHashesResponse(opResult)
    }


    @PutMapping("/")
    fun updHash(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: HashesDto
    ): HashesResponse {
        val opResult = with(model) {
            hashesService.update(id?:0, profile, main, duplicates)
        }

        return getHashesResponse(opResult)
    }

    @DeleteMapping("/")
    fun delHash(@RequestBody model: HashesDto): HashesResponse {
        val opResult = hashesService.remove(model.id ?: 0)

        return getHashesResponse(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpHashes(@RequestParam(required = true) profileId: Long): HashesResponse {
        val opResult = hashesService.cleanUp(profileId)

        return getHashesResponse(opResult)
    }


}