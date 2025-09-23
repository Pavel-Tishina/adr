package com.paveltsikota.webcore.rest.controller

import com.paveltsikota.webcore.db.service.FilesService
import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import com.paveltsikota.webcore.rest.model.DeleteFilesByIdsRequest
import com.paveltsikota.webcore.db.dto.FilesDto
import com.paveltsikota.webcore.rest.model.FilesResponse
import com.paveltsikota.webcore.rest.model.GetFilesByIdsRequest
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getFilesResponse
import org.springframework.web.bind.annotation.*
import kotlin.io.path.Path


@RestController
@RequestMapping("/rest/v1/test/db/files")
class TestDbFilesController(private val fileService: FilesService) {

    @GetMapping("/get/{id}")
    fun getFileById(@PathVariable id: Long): FilesResponse {
        val opResult = fileService.getFile(id)

        return getFilesResponse(opResult)
    }

    @GetMapping("/get")
    fun getFileByIds(@RequestBody ids: GetFilesByIdsRequest): FilesResponse {
        val opResult = fileService.getFiles(ids.ids)

        return getFilesResponse(opResult)
    }

    @GetMapping("/get-all")
    fun getFiles(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int,
        @RequestParam(required = false) profileId: Long?
    ): FilesResponse {
        val opResult = fileService.getFiles(page, pageSize, profileId)

        return getFilesResponse(opResult)
    }

    @PostMapping("/")
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

    @PutMapping("/")
    fun updFile(
        @RequestHeader("Upd-As-Local", defaultValue = "false") updAsLocal: Boolean,
        @RequestBody model: FilesDto
    ): FilesResponse {
        val opResult = fileService.updateFile(model, updAsLocal)

        return getFilesResponse(opResult)
    }

    @DeleteMapping("/remove")
    fun delFile(@RequestBody model: FilesDto): FilesResponse {
        val opResult = fileService.removeFile(model)

        return getFilesResponse(opResult)
    }

    @DeleteMapping("/remove-by-ids")
    fun delFile(@RequestBody model: DeleteFilesByIdsRequest): FilesResponse {
        val opResult = fileService.removeFiles(model.ids)

        return getFilesResponse(opResult)
    }

}