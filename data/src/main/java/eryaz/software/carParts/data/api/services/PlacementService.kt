package eryaz.software.carParts.data.api.services

import eryaz.software.carParts.data.models.remote.models.ResultModel
import eryaz.software.carParts.data.models.remote.response.WorkActionResponse
import eryaz.software.carParts.data.models.remote.response.WorkActivityResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PlacementService {

    @GET("api/services/app/Work/GetReceivingWorkActivityListForPdaPlacement")
    suspend fun getPlacementWorkActivityList(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int
        ): ResultModel<List<WorkActivityResponse>>

    @GET("api/services/app/Work/GetTransferRequestReceivingWorkActivityListForPdaPlacement")
    suspend fun getTransferRequestWorkActivityList(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int
        ): ResultModel<List<WorkActivityResponse>>

    //Var olan Work Action
    @GET("api/services/app/Work/GetWorkActionForPda")
    suspend fun getWorkAction(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int,
    ): ResultModel<WorkActionResponse>

    //Work Action oluşturma
    @POST("api/services/app/Work/CreateWorkAction")
    suspend fun createWorkAction(
        @Query("activityId") activityId: Int,
        @Query("actionTypeCode") actionTypeCode: String,
    ): ResultModel<WorkActionResponse>

}