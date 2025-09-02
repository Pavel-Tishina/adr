package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.mapper.EntityMapper
import com.paveltsikota.webcore.db.service.FilesService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.rest.api.FilesDto
import com.paveltsikota.webcore.rest.api.FilesResponse
import com.paveltsikota.webcore.utils.FilesEntityUtils
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/rest/v1/test")
class TestDbController(private val fileService: FilesService) {


    @GetMapping("/files/get/{id}")
    fun getFileById(@PathVariable id: String): FilesResponse {
        val idVal = id.toLong()
        val opResult = fileService.getFile(idVal)

        return getFilesResponse(opResult)
    }

    @PostMapping("/files")
    fun addFile(
        @RequestHeader("Force-Add") force: Boolean = false,
        @RequestHeader("Force-Calculate") calculate: Boolean = false,
        @RequestBody model: FilesDto): FilesResponse {
        val opResult = if (force) {
            fileService.addFile(model, calculate)
        } else {
            fileService.addFileIfNotExist(model, calculate)
        }

        return getFilesResponse(opResult)
    }

    @PutMapping("/files")
    fun updFile(@RequestBody model: FilesDto): FilesResponse {
        val opResult = fileService.updateFile(model)

        return getFilesResponse(opResult)
    }

    @DeleteMapping("/files")
    fun delFile(@RequestBody model: FilesDto): FilesResponse {
        val opResult = fileService.removeFile(model)

        return getFilesResponse(opResult)
    }

    private fun getFilesResponse(opResult: EntityOperationResult): FilesResponse {
        return FilesResponse(
            success = opResult.success,
            obj = EntityMapper.filesEntityToDtoList(opResult.obj),
            error = opResult.error ?: ""
        )
    }



}