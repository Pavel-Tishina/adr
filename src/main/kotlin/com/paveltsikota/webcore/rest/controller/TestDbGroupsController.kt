package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.dto.GroupsDto
import com.paveltsikota.webcore.db.service.GroupsService
import com.paveltsikota.webcore.rest.model.TypedResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getTypedResponse
import com.paveltsikota.webcore.db.adapter.GroupsAdapter
import com.paveltsikota.webcore.db.dto.CleanUpDto
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/rest/v1/test/db/groups")
class TestDbGroupsController(private val groupsService: GroupsService) {

    @GetMapping("/get/{id}")
    fun getGroupById(@PathVariable id: Long): TypedResponse<List<GroupsDto>> {
        val opResult = groupsService.getGroup(id)

        return getTypedResponse<List<GroupsDto>>(opResult)
    }

    @GetMapping("/get-all")
    fun getAllGroups(
        @RequestParam(required = false, defaultValue = "1") page: Int?,
        @RequestParam(required = false, defaultValue = "20") pageSize: Int?,
        @RequestParam(required = false) profileId: Long?
    ): TypedResponse<List<GroupsDto>> {
        val opResult = groupsService.getAllGroups(profileId).takeIf { page == null && pageSize == null}
            ?: groupsService.getGroups(page, pageSize, profileId)

        return getTypedResponse<List<GroupsDto>>(opResult)
    }

    @GetMapping("/get-by-size")
    fun getBySize(
        @RequestParam(required = true) size: Long,
        @RequestParam(required = true) profileId: Long
    ): TypedResponse<List<GroupsDto>> {
        val opResult = groupsService.findBySize(size, profileId)

        return getTypedResponse<List<GroupsDto>>(opResult)
    }

    @PostMapping("/")
    fun addGroup(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: GroupsDto
    ): TypedResponse<List<GroupsDto>> {
        val opResult = groupsService.addGroup(
            size = model.size,
            profileId = model.profile,
            fileIds = model.fileIds?: emptySet(),
            addOnce = addOnce
        )

        return getTypedResponse<List<GroupsDto>>(opResult)
    }

    @PutMapping("/")
    fun updGroup(
        @RequestHeader("Upd-As-Local", defaultValue = "false") updAsLocal: Boolean,
        @RequestBody model: GroupsDto
    ): TypedResponse<List<GroupsDto>> {
        val opResult = groupsService.updateGroup(GroupsAdapter.dtoToEntity(model))

        return getTypedResponse<List<GroupsDto>>(opResult)
    }

    @DeleteMapping("/remove")
    fun delGroup(@RequestBody model: GroupsDto): TypedResponse<List<GroupsDto>> {
        val opResult = groupsService.removeGroup(model.id ?: 0)

        return getTypedResponse<List<GroupsDto>>(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpGroups(@RequestParam profileId: Long): TypedResponse<CleanUpDto> {
        val opResult = groupsService.cleanUp(profileId)

        return getTypedResponse<CleanUpDto>(opResult)
    }


}