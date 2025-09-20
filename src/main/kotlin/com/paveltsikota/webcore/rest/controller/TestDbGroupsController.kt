package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.service.GroupsService
import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.rest.model.GroupsResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getGroupsResponse
import com.paveltsikota.webcore.utils.entity.GroupsEntityUtils
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/rest/v1/test/db/groups")
class TestDbGroupsController(private val groupsService: GroupsService) {

    @GetMapping("/get/{id}")
    fun getGroupById(@PathVariable id: String): GroupsResponse {
        val idVal = id.toLong()
        val opResult = groupsService.getGroup(idVal)

        return getGroupsResponse(opResult)
    }

    @GetMapping("/get-all")
    fun getAllGroups(
        @RequestParam(required = false, defaultValue = "1") page: Int?,
        @RequestParam(required = false, defaultValue = "20") pageSize: Int?,
        @RequestParam(required = false) profileId: Long?
    ): GroupsResponse {
        val opResult = groupsService.getAllGroups(profileId).takeIf { page == null && pageSize == null}
            ?: groupsService.getGroups(page, pageSize, profileId)

        return getGroupsResponse(opResult)
    }

    @GetMapping("/get-by-size")
    fun getBySize(
        @RequestParam(required = true) size: Long,
        @RequestParam(required = true) profileId: Long
    ): GroupsResponse {
        val opResult = groupsService.findBySize(size, profileId)

        return getGroupsResponse(opResult)
    }

    @PostMapping("/")
    fun addGroup(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: GroupsDto
    ): GroupsResponse {
        val opResult = groupsService.addGroup(
            size = model.size,
            profileId = model.profile,
            fileIds = model.fileIds?: emptySet(),
            addOnce = addOnce
        )

        return getGroupsResponse(opResult)
    }

    @PutMapping("/")
    fun updGroup(
        @RequestHeader("Upd-As-Local", defaultValue = "false") updAsLocal: Boolean,
        @RequestBody model: GroupsDto
    ): GroupsResponse {
        val opResult = groupsService.updateGroup(GroupsEntityUtils.dtoToEntity(model))

        return getGroupsResponse(opResult)
    }

    @DeleteMapping("/remove")
    fun delGroup(@RequestBody model: GroupsDto): GroupsResponse {
        val opResult = groupsService.removeGroup(model.id ?: 0)

        return getGroupsResponse(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpGroups(@RequestParam(required = true) profileId: Long): GroupsResponse {
        val opResult = groupsService.cleanUp(profileId)

        return getGroupsResponse(opResult)
    }


}