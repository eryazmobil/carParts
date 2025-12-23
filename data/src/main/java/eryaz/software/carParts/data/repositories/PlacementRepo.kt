package eryaz.software.carParts.data.repositories

import eryaz.software.carParts.data.api.utils.ResponseHandler
import eryaz.software.carParts.data.api.services.PlacementService
import eryaz.software.carParts.data.mappers.toDto

class PlacementRepo(private val api: PlacementService) : BaseRepo() {

    suspend fun getPlacementWorkActivityList(companyId:Int,warehouseId:Int) = callApi {
        val response = api.getPlacementWorkActivityList(companyId = companyId,warehouseId = warehouseId)
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getTransferRequestWorkActivityList(companyId:Int,warehouseId:Int) = callApi {
        val response = api.getTransferRequestWorkActivityList(companyId = companyId,warehouseId = warehouseId)
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getWorkAction(companyId:Int, warehouseId:Int) = callApi {
        val response = api.getWorkAction(companyId = companyId,warehouseId = warehouseId)
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun createWorkAction(activityId:Int, actionTypeCode:String) = callApi {
        val response = api.createWorkAction(activityId = activityId,actionTypeCode = actionTypeCode)
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

}