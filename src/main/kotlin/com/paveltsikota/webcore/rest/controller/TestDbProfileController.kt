package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.adapter.ProfileAdapter
import com.paveltsikota.webcore.db.dto.ProfileDto
import com.paveltsikota.webcore.db.service.ProfileService
import com.paveltsikota.webcore.db.utils.ConfigUtils.dtoToMap
import com.paveltsikota.webcore.rest.model.TypedResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getTypedResponse
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/rest/v1/test/db/profile")
class TestDbProfileController(private val profileService: ProfileService) {

    @GetMapping("/{id}")
    fun getProfileById(@PathVariable id: Long): TypedResponse<List<ProfileDto>> {
        val opResult = profileService.getById(id)

        return getTypedResponse<List< ProfileDto>>(opResult)
    }

    @GetMapping("/")
    fun getProfile(@RequestParam(required = true) title: String): TypedResponse<List<ProfileDto>> {
        val opResult = profileService.getByTitle(title)

        return getTypedResponse<List< ProfileDto>>(opResult)
    }

    @PostMapping("/")
    fun addProfile(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: ProfileDto
    ): TypedResponse<List< ProfileDto>> {
        val opResult = with(model) {
            profileService.add(title, description, dtoToMap(cfg), addOnce)
        }

        return getTypedResponse<List< ProfileDto>>(opResult)
    }

    @PutMapping("/")
    fun updProfile(@RequestBody model: ProfileDto): TypedResponse<List<ProfileDto>> {
        val opResult = profileService.update(ProfileAdapter.dtoToEntity(model))

        return getTypedResponse<List< ProfileDto>>(opResult)
    }

    @DeleteMapping("/")
    fun delProfile(@RequestBody model: ProfileDto): TypedResponse<List<ProfileDto>> {
        val opResult = profileService.remove(model.id ?: 0)

        return getTypedResponse<List<ProfileDto>>(opResult)
    }

}