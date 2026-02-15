package com.paveltsikota.webcore.rest.controller.test

import com.paveltsikota.webcore.db.dto.CleanUpDto
import com.paveltsikota.webcore.db.dto.FilesDto
import com.paveltsikota.webcore.db.service.FilesService
import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import com.paveltsikota.webcore.rest.model.DeleteFilesByIdsRequest
import com.paveltsikota.webcore.rest.model.GetFilesByIdsRequest
import com.paveltsikota.webcore.rest.model.TypedResponse
import com.paveltsikota.webcore.rest.utils.ResponseUtils.getTypedResponse
import org.springframework.web.bind.annotation.*
import kotlin.io.path.Path

@Deprecated(message = "Only 4 testing")
@RestController
@RequestMapping("/rest/v1/test/db/files")
class TestDbFilesController(private val fileService: FilesService) {


    @GetMapping("/get/{id}")
    fun getFileById(@PathVariable id: Long): TypedResponse<List<FilesDto>> {
        val opResult = fileService.getFile(id)

        return getTypedResponse<List<FilesDto>>(opResult)
    }

    @GetMapping("/get")
    fun getFileByIds(@RequestBody ids: GetFilesByIdsRequest): TypedResponse<List<FilesDto>> {
        val opResult = fileService.getFiles(ids.ids)

        return getTypedResponse<List<FilesDto>>(opResult)
    }

    @GetMapping("/get-all")
    fun getFiles(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int,
        @RequestParam(required = false) profileId: Long?
    ): TypedResponse<List<FilesDto>> {
        val opResult = fileService.getFiles(page, pageSize, profileId)

        return getTypedResponse<List<FilesDto>>(opResult)
    }

    @PostMapping("/")
    fun addFile(
        @RequestHeader("Add-Once", defaultValue = "true") addOnce: Boolean,
        @RequestHeader("Add-As-Local", defaultValue = "false") addAsLocal: Boolean,
        @RequestBody model: FilesDto
    ): TypedResponse<List<FilesDto>> {
        val opResult = if (addAsLocal) {
            fileService.addLocalFile(Path(model.path?: ""), XXHash64, addOnce)
        } else {
            fileService.addRemoteFile(model, addOnce)
        }

        return getTypedResponse<List<FilesDto>>(opResult)
    }

    @PutMapping("/")
    fun updFile(
        @RequestHeader("Upd-As-Local", defaultValue = "false") updAsLocal: Boolean,
        @RequestBody model: FilesDto
    ): TypedResponse<List<FilesDto>> {
        val opResult = fileService.updateFile(model, updAsLocal)

        return getTypedResponse<List<FilesDto>>(opResult)
    }

    @DeleteMapping("/remove")
    fun delFile(@RequestBody model: FilesDto): TypedResponse<List<FilesDto>> {
        val opResult = fileService.removeFile(model)

        return getTypedResponse<List<FilesDto>>(opResult)
    }

    @DeleteMapping("/remove-by-ids")
    fun delFile(@RequestBody model: DeleteFilesByIdsRequest): TypedResponse<CleanUpDto> {
        val opResult = fileService.removeFiles(model.ids)

        return getTypedResponse<CleanUpDto>(opResult)
    }

}