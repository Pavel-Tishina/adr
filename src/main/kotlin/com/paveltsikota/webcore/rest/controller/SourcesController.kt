package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.adapter.SourcesAdapter
import com.paveltsikota.webcore.db.dto.CleanUpDto
import com.paveltsikota.webcore.db.dto.SourcesDto
import com.paveltsikota.webcore.db.service.SourcesService
import com.paveltsikota.webcore.rest.model.ManySourcesRequest
import com.paveltsikota.webcore.rest.model.TypedResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getTypedResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.io.path.Path

@RestController
@RequestMapping("/rest/v1/sources")
class SourcesController(private val sourcesService: SourcesService) {
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
        val opResult = sourcesService.addSourcesDto(model.sources, addOnce)

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