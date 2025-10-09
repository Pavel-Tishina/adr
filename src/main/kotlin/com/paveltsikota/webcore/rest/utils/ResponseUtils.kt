package com.paveltsikota.webcore.rest.utils

import com.paveltsikota.webcore.db.mapper.EntityMapper
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.rest.model.CommonResponse
import com.paveltsikota.webcore.rest.model.FilesResponse
import com.paveltsikota.webcore.rest.model.GroupsResponse
import com.paveltsikota.webcore.rest.model.HashesResponse
import com.paveltsikota.webcore.rest.model.JobsResponse
import com.paveltsikota.webcore.rest.model.ProfileResponse
import com.paveltsikota.webcore.rest.model.SourcesResponse
import com.paveltsikota.webcore.rest.model.TypedResponse
import java.util.Objects
import kotlin.collections.isNotEmpty

object ResponseUtils {

//    fun getFilesResponse(opResult: EntityOperationResult): FilesResponse {
//        val resultObj = EntityMapper.filesEntityToDtoList(opResult.obj)
//        return FilesResponse(
//            success = opResult.success,
//            obj = resultObj.takeIf { resultObj.isNotEmpty() },
//            error = opResult.error ?: ""
//        )
//    }
//
//    fun getGroupsResponse(opResult: EntityOperationResult): GroupsResponse {
//        val resultObj = EntityMapper.groupsEntityToDtoList(opResult.obj)
//        return GroupsResponse(
//            success = opResult.success,
//            obj = resultObj.takeIf { resultObj.isNotEmpty() },
//            error = opResult.error ?: ""
//        )
//    }
//
//    fun getHashesResponse(opResult: EntityOperationResult): HashesResponse {
//        val resultObj = EntityMapper.hashesEntityToDtoList(opResult.obj)
//        return HashesResponse(
//            success = opResult.success,
//            obj = resultObj.takeIf { resultObj.isNotEmpty() },
//            error = opResult.error ?: ""
//        )
//    }
//
//    fun getJobsResponse(opResult: EntityOperationResult): JobsResponse {
//        val resultObj = EntityMapper.jobsEntityToDtoList(opResult.obj)
//        return JobsResponse(
//            success = opResult.success,
//            obj = resultObj.takeIf { resultObj.isNotEmpty() },
//            error = opResult.error ?: ""
//        )
//    }
//
//    fun getProfileResponse(opResult: EntityOperationResult): ProfileResponse {
//        val resultObj = EntityMapper.profileEntityToDtoList(opResult.obj)
//        return ProfileResponse(
//            success = opResult.success,
//            obj = resultObj.takeIf { resultObj.isNotEmpty() },
//            error = opResult.error ?: ""
//        )
//    }
//
//    fun getSourcesResponse(opResult: EntityOperationResult): SourcesResponse {
//        val resultObj = EntityMapper.sourceEntityToDtoList(opResult.obj)
//        return SourcesResponse(
//            success = opResult.success,
//            obj = resultObj.takeIf { resultObj.isNotEmpty() },
//            error = opResult.error ?: ""
//        )
//    }

    fun getCommonResponse(opResult: EntityOperationResult): CommonResponse {
        val resultObj = EntityMapper.anyToDtoList(opResult.obj)
        return CommonResponse(
            success = opResult.success,
            obj = resultObj.takeIf { resultObj.isNotEmpty() },
            error = opResult.error ?: ""
        )
    }

    inline fun <reified T> getTypedResponse(opResult: EntityOperationResult): TypedResponse<T> {
        val resultObj = if (!Objects.isNull(opResult.obj)) {
            EntityMapper.anyToTypedDto<T>(opResult.obj)
        } else {
            null
        }

        return TypedResponse<T>(
            success = opResult.success,
            obj = if (Objects.isNull(resultObj)) { null } else { resultObj as T },
            error = opResult.error ?: ""
        )
    }

}