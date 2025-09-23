package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.dto.ProfileDto
import com.paveltsikota.webcore.db.service.ProfileService
import com.paveltsikota.webcore.rest.model.ProfileResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getProfileResponse
import com.paveltsikota.webcore.utils.entity.ConfigEntityUtils
import com.paveltsikota.webcore.utils.entity.ProfileEntityUtils
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/rest/v1/test/db/profile")
class TestDbProfileController(private val profileService: ProfileService) {

    @GetMapping("/{id}")
    fun getProfileById(@PathVariable id: Long): ProfileResponse {
        val opResult = profileService.getById(id)

        return getProfileResponse(opResult)
    }

    // Dirty... too dirty... but it's just for test repository!
    @GetMapping("/")
    fun getProfile(@RequestParam(required = true) title: String): ProfileResponse {
        val opResult = profileService.getByTitle(title)

        return getProfileResponse(opResult)
    }

    @PostMapping("/")
    fun addProfile(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: ProfileDto
    ): ProfileResponse {
        val opResult = with(model) {
            profileService.add(title, description, ConfigEntityUtils.dtoToMap(cfg, id ?: 0), addOnce)
        }

        return getProfileResponse(opResult)
    }

    @PutMapping("/")
    fun updProfile(@RequestBody model: ProfileDto): ProfileResponse {
        val opResult = profileService.update(ProfileEntityUtils.dtoToEntity(model))

        return getProfileResponse(opResult)
    }

    @DeleteMapping("/")
    fun delProfile(@RequestBody model: ProfileDto): ProfileResponse {
        val opResult = profileService.remove(model.id ?: 0)

        return getProfileResponse(opResult)
    }

}