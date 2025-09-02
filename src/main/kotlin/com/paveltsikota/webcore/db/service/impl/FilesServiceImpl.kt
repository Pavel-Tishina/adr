package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.entity.FilesEntity
import com.paveltsikota.webcore.db.service.FilesService
import com.paveltsikota.webcore.db.dao.FilesDao
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.db.service.result.enums.EntityOperationResultType
import com.paveltsikota.webcore.hash.calculator.impl.XXHash64
import com.paveltsikota.webcore.rest.api.FilesDto
import com.paveltsikota.webcore.utils.FilesEntityUtils
import com.paveltsikota.webcore.utils.FilesEntityUtils.eq
import com.paveltsikota.webcore.utils.enums.FileState
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class FilesServiceImpl(private val filesDao: FilesDao): FilesService {
    val xxHash64 = XXHash64

    override fun getFile(id: Long): EntityOperationResult {
        val entity = filesDao.findById(id)

        return if (entity != null) {
            EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_FOUND)
        } else {
            EntityOperationResult(success = false, result = EntityOperationResultType.ENTITY_NOT_FOUND)
        }
    }

    override fun addFile(path: String): EntityOperationResult {
        return if (path.isNotBlank()) {
            addFile(Path.of(path))
        } else {
            EntityOperationResult(
                success = false,
                error = "Path is empty",
                result = EntityOperationResultType.ENTITY_NOT_ADD)
        }
    }

    override fun addFile(path: Path): EntityOperationResult {
        val entity = FilesEntityUtils.getFilesEntryByPathForDb(path, xxHash64)

        return if (entity.state == FileState.ON_PLACE) {
            filesDao.save(entity)
            EntityOperationResult(success = true, result = EntityOperationResultType.ENTITY_ADD)
        } else {
            EntityOperationResult(success = false, result = EntityOperationResultType.ENTITY_NOT_ADD)
        }
    }

    override fun addFile(fileDto: FilesDto, calculateHash: Boolean): EntityOperationResult {
        return if (calculateHash && fileDto.path.isNotBlank() && fileDto.hash.isNullOrBlank() && fileDto.hashType == null) {
            val entity = FilesEntityUtils.getFilesEntryByPathForDb(fileDto.path, xxHash64)

            if (entity.state != FileState.NOT_FOUND) {
                filesDao.save(entity)
                EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_ADD)
            } else {
                EntityOperationResult(success = false, obj = entity, error = "file not found", result = EntityOperationResultType.ENTITY_NOT_ADD)
            }
        } else {
            val entity = FilesEntityUtils.dtoToEntity(fileDto)
            if (FilesEntityUtils.validateEntityForAdd(entity)) {
                filesDao.save(entity)
                EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_ADD)
            } else {
                EntityOperationResult(success = false, obj = entity, result = EntityOperationResultType.ENTITY_NOT_ADD)
            }
        }
    }

    override fun addFileIfNotExist(path: Path): EntityOperationResult {
        val entity = FilesEntityUtils.getFilesEntryByPathForDb(path, xxHash64)
        return with (entity) {
            if (filesDao.checkAlreadyExistEntity(path, size, hash, hashType, profile)) {
                EntityOperationResult(success = false, obj = entity, result = EntityOperationResultType.ENTITY_ALREADY_EXIST)
            } else {
                filesDao.save(entity)
                EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_ADD)
            }
        }
    }

    override fun addFileIfNotExist(fileDto: FilesDto, calculateHash: Boolean): EntityOperationResult {
        val entity = FilesEntityUtils.dtoToEntity(fileDto)
        return with (entity) {
            if (filesDao.checkAlreadyExistEntity(Path.of(path), size, hash, hashType, profile)) {
                EntityOperationResult(success = false, obj = entity, error = "Entity already exist", result = EntityOperationResultType.ENTITY_ALREADY_EXIST)
            } else {
                filesDao.save(entity)
                EntityOperationResult(success = true, obj = entity, result = EntityOperationResultType.ENTITY_ADD)
            }
        }
    }

    override fun addLocalFile(path: Path): EntityOperationResult {
        TODO("Not yet implemented")
    }

    override fun addRemoteFile(fileDto: FilesDto): EntityOperationResult {
        TODO("Not yet implemented")
    }

    override fun updateFile(file: FilesEntity): EntityOperationResult {
        val obj = filesDao.update(file)
        return if (eq(file, obj)) {
            EntityOperationResult(success = true, obj = obj, result = EntityOperationResultType.ENTITY_UPDATED)
        } else {
            EntityOperationResult(success = false, obj = obj, result = EntityOperationResultType.ENTITY_NOT_UPDATED)
        }
    }

    override fun updateFile(fileFto: FilesDto): EntityOperationResult {
        return updateFile(FilesEntityUtils.dtoToEntity(fileFto))
    }

    override fun removeFile(file: FilesEntity): EntityOperationResult {
        return removeFile(file.id)
    }

    override fun removeFile(fileDto: FilesDto): EntityOperationResult {
        return if (fileDto.id != null) {
            removeFile(fileDto.id)
        } else {
            EntityOperationResult(success = false, error = "id is null", result = EntityOperationResultType.ENTITY_NOT_REMOVED)
        }
    }

    override fun removeFile(id: Long): EntityOperationResult {
        return if (filesDao.removeById(id)) {
            EntityOperationResult(success = true, result = EntityOperationResultType.ENTITY_REMOVED)
        } else {
            EntityOperationResult(success = false, obj = id, result = EntityOperationResultType.ENTITY_NOT_REMOVED)
        }
    }
}