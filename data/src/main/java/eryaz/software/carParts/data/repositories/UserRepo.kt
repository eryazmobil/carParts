package eryaz.software.carParts.data.repositories

import eryaz.software.carParts.data.api.services.UserApiService
import eryaz.software.carParts.data.api.utils.ResponseHandler
import eryaz.software.carParts.data.mappers.toDto
import eryaz.software.carParts.data.models.remote.request.ChangePasswordRequest

class UserRepo(private val api: UserApiService) : BaseRepo() {

    suspend fun getCurrentLoginHasPermissionsForPDAMenu() = callApi {
        val response = api.getCurrentLoginHasPermissionsForPDAMenu()
        ResponseHandler.handleSuccess(response, response.result)
    }

    suspend fun getCurrentLoginHasPermission(permission: String) = callApi {
        val response = api.getCurrentLoginHasPermission(permission)
        ResponseHandler.handleSuccess(response, response.result)
    }

    suspend fun getCurrentLoginInformations() = callApi {
        val response = api.getCurrentLoginInformations()
        ResponseHandler.handleSuccess(response, response.result.userInfo.toDto())
    }

    suspend fun getCompanyList() = callApi {
        val response = api.getCompanyList()
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getWarehouseList(companyId:Int) = callApi {
        val response = api.getWarehouseList(companyId)
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getStorageListByWarehouse(warehouseId: Int) = callApi {
        val response = api.getStorageListByWarehouse(warehouseId)
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest) = callApi {
        val response = api.changePassword(changePasswordRequest)
        ResponseHandler.handleSuccess(response, response.result)
    }

    suspend fun fetchWorkActionTypeList() = callApi {
        val response = api.fetchWorkActionTypeList()
        ResponseHandler.handleSuccess(response, response.result)
    }
}