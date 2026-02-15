package com.paveltsikota.webcore.rest.controller.test

import com.paveltsikota.webcore.db.dto.CleanUpDto
import com.paveltsikota.webcore.db.dto.HashesDto
import com.paveltsikota.webcore.db.service.HashesService
import com.paveltsikota.webcore.rest.model.PostGetManyHashesRequest
import com.paveltsikota.webcore.rest.model.TypedResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getTypedResponse
import com.paveltsikota.webcore.utils.ValuesUtils.validateFindByHash
import com.paveltsikota.webcore.utils.enums.HashType
import org.springframework.web.bind.annotation.*

@Deprecated(message = "Only 4 testing")
@RestController
@RequestMapping("/rest/v1/test/db/hashes")
class TestDbHashesController(private val hashesService: HashesService) {

    @GetMapping("/{id}")
    fun getHashById(@PathVariable id: Long): TypedResponse<List<HashesDto>> {
        val opResult = hashesService.getById(id)

        return getTypedResponse<List<HashesDto>>(opResult)
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
    ): TypedResponse<List<HashesDto>> {
        val opResult = when {
            validateFindByHash(hash, hashType, profileId)
                -> hashesService.findByHash(hash!!, hashType!!, profileId!!)
            size != null && profileId != null
                -> hashesService.findBySize(page, pageSize, size, profileId)
            else
                -> hashesService.getAllByN(page, pageSize, profileId, n)
        }

        return getTypedResponse<List<HashesDto>>(opResult)
    }

    @PostMapping("/many")
    fun getManyHashes(@RequestBody model: PostGetManyHashesRequest): TypedResponse<List<HashesDto>> {
        val opResult = hashesService.getByIds(model.ids)

        return getTypedResponse<List<HashesDto>>(opResult)
    }

    @PostMapping("/")
    fun addHash(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: HashesDto
    ): TypedResponse<List<HashesDto>> {
        val opResult = with(model) {
            hashesService.add(profile, size, hash, hashType, main, duplicates, addOnce)
        }

        return getTypedResponse<List<HashesDto>>(opResult)
    }

    @PutMapping("/")
    fun updHash(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: HashesDto
    ): TypedResponse<List<HashesDto>> {
        val opResult = with(model) {
            hashesService.update(id?:0, profile, main, duplicates)
        }

        return getTypedResponse<List<HashesDto>>(opResult)
    }

    @DeleteMapping("/")
    fun delHash(@RequestBody model: HashesDto): TypedResponse<List<HashesDto>> {
        val opResult = hashesService.remove(model.id ?: 0)

        return getTypedResponse<List<HashesDto>>(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpHashes(@RequestParam(required = true) profileId: Long): TypedResponse<CleanUpDto> {
        val opResult = hashesService.cleanUp(profileId)

        return getTypedResponse<CleanUpDto>(opResult)
    }


}