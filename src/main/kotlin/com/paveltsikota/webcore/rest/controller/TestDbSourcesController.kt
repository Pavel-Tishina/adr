package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.dto.SourcesDto
import com.paveltsikota.webcore.db.service.SourcesService
import com.paveltsikota.webcore.rest.model.ManySourcesRequest
import com.paveltsikota.webcore.rest.model.SourcesResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getSourcesResponse
import com.paveltsikota.webcore.utils.entity.SourcesEntityUtils
import org.springframework.web.bind.annotation.*
import kotlin.io.path.Path


@RestController
@RequestMapping("/rest/v1/test/db/sources")
class TestDbSourcesController(private val sourcesService: SourcesService) {

    @GetMapping("/get/{id}")
    fun getSourceById(@PathVariable id: Long): SourcesResponse {
        val opResult = sourcesService.getSource(id)

        return getSourcesResponse(opResult)
    }

    @GetMapping("/")
    fun getJobs(
        @RequestParam(required = false) profileId: Long?,
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?
    ): SourcesResponse {
        val opResult = sourcesService.getSources(page, pageSize, profileId)

        return getSourcesResponse(opResult)
    }

    @PostMapping("/")
    fun addSource(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: SourcesDto
    ): SourcesResponse {
        val opResult = with(model) {
            sourcesService.addSource(Path(path), profile, dirorder, addOnce)
        }

        return getSourcesResponse(opResult)
    }

    @PostMapping("/")
    fun addSources(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: ManySourcesRequest
    ): SourcesResponse {
        val opResult = with(model) {
            sourcesService.addSourcesDto(model.sources, addOnce)
        }

        return getSourcesResponse(opResult)
    }

    @PutMapping("/")
    fun updSource(@RequestBody model: SourcesDto): SourcesResponse {
        val opResult = sourcesService.updateSource(SourcesEntityUtils.dtoToEntity(model))

        return getSourcesResponse(opResult)
    }

    @PutMapping("/many")
    fun updSources(@RequestBody model: ManySourcesRequest): SourcesResponse {
        val opResult = sourcesService.updateSourcesDto(model.sources)

        return getSourcesResponse(opResult)
    }

    @DeleteMapping("/")
    fun delSource(@RequestBody model: SourcesDto): SourcesResponse {
        val opResult = sourcesService.removeSource(model.id ?: 0)

        return getSourcesResponse(opResult)
    }

    @DeleteMapping("/many")
    fun delSources(@RequestBody model: ManySourcesRequest): SourcesResponse {
        val opResult = sourcesService.removeSourcesDto(model.sources)

        return getSourcesResponse(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpGroups(@RequestParam(required = true) profileId: Long): SourcesResponse {
        val opResult = sourcesService.cleanUp(profileId)

        return getSourcesResponse(opResult)
    }

}