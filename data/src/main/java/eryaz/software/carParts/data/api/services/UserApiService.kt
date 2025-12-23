package eryaz.software.carParts.data.api.services

import eryaz.software.carParts.data.models.remote.models.ResultModel
import eryaz.software.carParts.data.models.remote.request.ChangePasswordRequest
import eryaz.software.carParts.data.models.remote.response.CompanyResponse
import eryaz.software.carParts.data.models.remote.response.CurrentLoginResponse
import eryaz.software.carParts.data.models.remote.response.StorageResponse
import eryaz.software.carParts.data.models.remote.response.WarehouseResponse
import eryaz.software.carParts.data.models.remote.response.WorkActionTypeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApiService {

    @GET("api/services/app/Session/GetCurrentLoginHasPermissionsForPDAMenu")
    suspend fun getCurrentLoginHasPermissionsForPDAMenu(): ResultModel<List<String>>

    @GET("api/services/app/Session/GetCurrentLoginHasPermission")
    suspend fun getCurrentLoginHasPermission(@Body permission: String): ResultModel<Boolean>

    @GET("api/services/app/Session/GetCurrentLoginInformations")
    suspend fun getCurrentLoginInformations(): ResultModel<CurrentLoginResponse>

    @GET("api/services/app/Company/GetCompanyList")
    suspend fun getCompanyList(): ResultModel<List<CompanyResponse>>

    @GET("api/services/app/Warehouse/GetWarehouseList")
    suspend fun getWarehouseList(@Query("companyId") companyId: Int): ResultModel<List<WarehouseResponse>>

    @GET("api/services/app/Warehouse/GetStorageListByWarehouse")
    suspend fun getStorageListByWarehouse(@Query("warehouseId") warehouseId: Int): ResultModel<List<StorageResponse>>

    @POST("api/services/app/User/ChangePassword")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): ResultModel<Boolean>

    @GET("api/services/app/Work/GetWorkActionTypeList")
    suspend fun fetchWorkActionTypeList(): ResultModel<List<WorkActionTypeResponse>>

}