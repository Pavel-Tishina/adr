package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.mapper.EntityMapper
import com.paveltsikota.webcore.db.service.FilesService
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import com.paveltsikota.webcore.rest.api.DeleteFilesByIdsRequest
import com.paveltsikota.webcore.rest.api.FilesDto
import com.paveltsikota.webcore.rest.api.FilesResponse
import com.paveltsikota.webcore.rest.api.GetFilesByIdsRequest
import org.springframework.web.bind.annotation.*
import kotlin.io.path.Path


@RestController
@RequestMapping("/rest/v1/test")
class TestDbController(private val fileService: FilesService) {


    @GetMapping("/files/get/{id}")
    fun getFileById(@PathVariable id: String): FilesResponse {
        val idVal = id.toLong()
        val opResult = fileService.getFile(idVal)

        return getFilesResponse(opResult)
    }

    @GetMapping("/files/get")
    fun getFileByIds(@RequestBody ids: GetFilesByIdsRequest): FilesResponse {
        val opResult = fileService.getFiles(ids.ids)

        return getFilesResponse(opResult)
    }

    @GetMapping("/files/get-all")
    fun getFiles(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int,
        @RequestParam(required = false) profileId: Long?
    ): FilesResponse {
        val opResult = fileService.getFiles(page, pageSize, profileId)

        return getFilesResponse(opResult)
    }

    @PostMapping("/files")
    fun addFile(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestHeader("Add-As-Local", defaultValue = "false") addAsLocal: Boolean,
        @RequestBody model: FilesDto
    ): FilesResponse {
        val opResult = if (addAsLocal) {
            fileService.addLocalFile(Path(model.path?: ""), XXHash64, addOnce)
        } else {
            fileService.addRemoteFile(model, addOnce)
        }

        return getFilesResponse(opResult)
    }

    @PutMapping("/files")
    fun updFile(
        @RequestHeader("Upd-As-Local", defaultValue = "false") updAsLocal: Boolean,
        @RequestBody model: FilesDto
    ): FilesResponse {
        val opResult = fileService.updateFile(model, updAsLocal)

        return getFilesResponse(opResult)
    }

    @DeleteMapping("/files/remove")
    fun delFile(@RequestBody model: FilesDto): FilesResponse {
        val opResult = fileService.removeFile(model)

        return getFilesResponse(opResult)
    }

    @DeleteMapping("/files/remove-by-ids")
    fun delFile(@RequestBody model: DeleteFilesByIdsRequest): FilesResponse {
        val opResult = fileService.removeFiles(model.ids)

        return getFilesResponse(opResult)
    }

    private fun getFilesResponse(opResult: EntityOperationResult): FilesResponse {
        val resultObj = EntityMapper.filesEntityToDtoList(opResult.obj)
        return FilesResponse(
            success = opResult.success,
            obj = resultObj.takeIf { resultObj.isNotEmpty() },
            error = opResult.error ?: ""
        )
    }



}