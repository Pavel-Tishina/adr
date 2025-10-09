package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.dto.ProfileDto
import com.paveltsikota.webcore.db.service.ProfileService
import com.paveltsikota.webcore.rest.model.TypedResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getTypedResponse
import com.paveltsikota.webcore.utils.entity.ConfigEntityUtils
import com.paveltsikota.webcore.utils.entity.ProfileEntityUtils
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
            profileService.add(title, description, ConfigEntityUtils.dtoToMap(cfg), addOnce)
        }

        return getTypedResponse<List< ProfileDto>>(opResult)
    }

    @PutMapping("/")
    fun updProfile(@RequestBody model: ProfileDto): TypedResponse<List<ProfileDto>> {
        val opResult = profileService.update(ProfileEntityUtils.dtoToEntity(model))

        return getTypedResponse<List< ProfileDto>>(opResult)
    }

    @DeleteMapping("/")
    fun delProfile(@RequestBody model: ProfileDto): TypedResponse<List<ProfileDto>> {
        val opResult = profileService.remove(model.id ?: 0)

        return getTypedResponse<List<ProfileDto>>(opResult)
    }

}