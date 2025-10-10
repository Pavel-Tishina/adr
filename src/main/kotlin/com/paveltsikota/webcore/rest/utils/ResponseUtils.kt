package com.paveltsikota.webcore.rest.utils

import com.paveltsikota.webcore.db.mapper.EntityMapper
import com.paveltsikota.webcore.db.service.result.EntityOperationResult
import com.paveltsikota.webcore.rest.model.CommonResponse
import com.paveltsikota.webcore.rest.model.TypedResponse
import java.util.*

object ResponseUtils {

    fun getCommonResponse(opResult: EntityOperationResult): CommonResponse {
        val resultObj = EntityMapper.anyToDtoList(opResult.obj)
        return CommonResponse(
            success = opResult.success,
            obj = resultObj.takeIf { resultObj.isNotEmpty() },
            error = opResult.error ?: ""
        )
    }

    inline fun <reified T : Any> getTypedResponse(opResult: EntityOperationResult): TypedResponse<T> {
        val resultObj = when(!Objects.isNull(opResult.obj)) {
            true -> EntityMapper.anyToTypedDto<T>(opResult.obj)
            else -> null
        }

        return TypedResponse<T>(
            success = opResult.success,
            obj = if (Objects.isNull(resultObj)) { null } else { resultObj as T },
            error = opResult.error ?: ""
        )
    }

}