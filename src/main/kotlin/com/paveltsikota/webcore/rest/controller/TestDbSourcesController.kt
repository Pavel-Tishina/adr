package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.dto.SourcesDto
import com.paveltsikota.webcore.db.service.SourcesService
import com.paveltsikota.webcore.rest.model.ManySourcesRequest
import com.paveltsikota.webcore.rest.model.TypedResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getTypedResponse
import com.paveltsikota.webcore.db.adapter.SourcesAdapter
import com.paveltsikota.webcore.db.dto.CleanUpDto
import org.springframework.web.bind.annotation.*
import kotlin.io.path.Path


@RestController
@RequestMapping("/rest/v1/test/db/sources")
class TestDbSourcesController(private val sourcesService: SourcesService) {

    @GetMapping("/get/{id}")
    fun getSourceById(@PathVariable id: Long): TypedResponse<List<SourcesDto>> {
        val opResult = sourcesService.getSource(id)

        return getTypedResponse<List<SourcesDto>>(opResult)
    }

    @GetMapping("/")
    fun getSources(
        @RequestParam(required = false) profileId: Long?,
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?
    ): TypedResponse<List<SourcesDto>> {
        val opResult = sourcesService.getSources(page, pageSize, profileId)

        return getTypedResponse<List<SourcesDto>>(opResult)
    }

    @PostMapping("/")
    fun addSource(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: SourcesDto
    ): TypedResponse<List<SourcesDto>> {
        val opResult = with(model) {
            sourcesService.addSource(Path(path), profile, dirorder, addOnce)
        }

        return getTypedResponse<List<SourcesDto>>(opResult)
    }

    @PostMapping("/many")
    fun addSources(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestBody model: ManySourcesRequest
    ): TypedResponse<List<SourcesDto>> {
        val opResult = with(model) {
            sourcesService.addSourcesDto(model.sources, addOnce)
        }

        return getTypedResponse<List<SourcesDto>>(opResult)
    }

    @PutMapping("/")
    fun updSource(@RequestBody model: SourcesDto): TypedResponse<List<SourcesDto>> {
        val opResult = sourcesService.updateSource(SourcesAdapter.dtoToEntity(model))

        return getTypedResponse<List<SourcesDto>>(opResult)
    }

    @PutMapping("/many")
    fun updSources(@RequestBody model: ManySourcesRequest): TypedResponse<List<SourcesDto>> {
        val opResult = sourcesService.updateSourcesDto(model.sources)

        return getTypedResponse<List<SourcesDto>>(opResult)
    }

    @DeleteMapping("/")
    fun delSource(@RequestBody model: SourcesDto): TypedResponse<List<SourcesDto>> {
        val opResult = sourcesService.removeSource(model.id ?: 0)

        return getTypedResponse<List<SourcesDto>>(opResult)
    }

    @DeleteMapping("/many")
    fun delSources(@RequestBody model: ManySourcesRequest): TypedResponse<List<SourcesDto>> {
        val opResult = sourcesService.removeSourcesDto(model.sources)

        return getTypedResponse<List<SourcesDto>>(opResult)
    }

    @DeleteMapping("/cleanup")
    fun cleanUpSources(@RequestParam(required = true) profileId: Long): TypedResponse<CleanUpDto> {
        val opResult = sourcesService.cleanUp(profileId)

        return getTypedResponse<CleanUpDto>(opResult)
    }

}