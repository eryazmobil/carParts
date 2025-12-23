package eryaz.software.carParts.data.api.services

import eryaz.software.carParts.data.models.dto.StockTakingDetailDto
import eryaz.software.carParts.data.models.remote.models.ResultModel
import eryaz.software.carParts.data.models.remote.request.FastCountingProcessRequestModel
import eryaz.software.carParts.data.models.remote.response.BaseResponse
import eryaz.software.carParts.data.models.remote.response.IdResponse
import eryaz.software.carParts.data.models.remote.response.ProductShelfQuantityResponse
import eryaz.software.carParts.data.models.remote.response.StockTackingProcessResponse
import eryaz.software.carParts.data.models.remote.response.StockTakingDetailResponse
import eryaz.software.carParts.data.models.remote.response.StockTakingHeaderResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface CountingService {

    @GET("api/services/app/StockTaking/GetIncompleteStockTakingHeaderListforPda")
    suspend fun getCountingWorkActivityList(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int
    ): ResultModel<List<StockTakingHeaderResponse>>

    @GET("api/services/app/StockTaking/GetSTActionProcessListForShelfForPda")
    suspend fun getSTActionProcessListForShelf(
        @Query("stHeaderId") stHeaderId: Int,
        @Query("AssignedShelfId") assignedShelfId: Int
    ): ResultModel<List<StockTackingProcessResponse>>

    @GET("api/services/app/StockTaking/GetShelfIsOnAssignedUserAfterBarcodeScanForStockTakingForPda")
    suspend fun getShelfIsOnAssignedUser(
        @Query("stHeaderId") stHeaderId: Int,
        @Query("ProcessUserId") processUserId: Int,
        @Query("shelfId") shelfId: Int
    ): ResultModel<Boolean>

    @POST("api/services/app/StockTaking/CreateSTActionProcessFromSTDetailShelfScanForPda")
    suspend fun createSTActionProcessFromSTDetail(
        @Query("stHeaderId") stHeaderId: Int,
        @Query("AssignedShelfId") assignedShelfId: Int,
        @Query("ProcessUserId") processUserId: Int
    ): BaseResponse

    @POST("api/services/app/StockTaking/FinishSTDetailAfterShelfCountFinishedForPda")
    suspend fun finishSTDetail(
        @Query("stDetailId") stDetailId: Int,
        @Query("ProcessUserId") userId: Int
    ): BaseResponse

    @PUT("api/services/app/StockTaking/UpdateSTActionProcessAddQuantityForPda")
    suspend fun addProduct(
        @Query("stHeaderId") stHeaderId: Int,
        @Query("AssignedShelfId") assignedShelfId: Int,
        @Query("ProductId") productId: Int,
        @Query("CountedQuantity") countedQuantity: Int,
        @Query("ProcessUserId") userId: Int
    ): BaseResponse

    @PUT("api/services/app/StockTaking/UpdateSTActionProcesstoCorrectQuantityForPda")
    suspend fun updateQuantitySTActionProcess(
        @Query("stActionProcessId") stActionProcessId: Int,
        @Query("AssignedShelfId") assignedShelfId: Int,
        @Query("ProductId") productId: Int,
        @Query("CountedQuantity") countedQuantity: Int,
    ): BaseResponse

    @DELETE("api/services/app/StockTaking/DeleteSTActionProcessAtWrongProductRecordForPda")
    suspend fun deleteSTActionProcess(
        @Query("stActionProcessId") stActionProcessId: Int,
    ): BaseResponse

    @GET("api/services/app/StockTaking/GetAllAssignedDetailsToUserForPda")
    suspend fun getAllAssignedToUser(
        @Query("stHeaderId") stActionProcessId: Int,
        @Query("ProcessUserId") processUserId: Int
    ): ResultModel<List<StockTakingDetailDto>>

    @POST("api/services/app/StockTaking/IsSuitableShelfForFastStockTaking")
    suspend fun isSuitableShelf(
        @Query("stHeaderId") stHeaderId: Int,
        @Query("shelfId") shelfId: Int
    ): ResultModel<Boolean>

    @GET("api/services/app/StockTaking/GetAllAssignedDetailsToUserForPdaForReCounting")
    suspend fun getAllAssignedDetailsToUser(
        @Query("stHeaderId") stHeaderId: Int,
        @Query("shelfId") shelfId: Int,
        @Query("userId") userId: Int
    ): ResultModel<List<StockTakingDetailResponse>>

    @GET("api/services/app/Product/GetProductQuantityListWithShelfForCounting")
    suspend fun getProductQuantityListWithShelf(
        @Query("productId") productId: Int,
        @Query("shelfId") shelfId: Int,
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int,
        @Query("stHeaderId") stHeaderId: Int,
    ): ResultModel<List<ProductShelfQuantityResponse>>

    @POST("api/services/app/StockTaking/FinishFastStocktakingDetail")
    suspend fun finishFastStocktakingDetail(
        @Body fastCountingProcessRequest: FastCountingProcessRequestModel
    ): BaseResponse

    @GET("api/services/app/StockTaking/GetPdaPartialStockTakingWorkActivityAndSTHeader")
    suspend fun getPdaPartialStockTakingWorkActivityAndSTHeader(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int
    ): ResultModel<IdResponse>

    @POST("api/services/app/StockTaking/CreateSTDetailWithUserAndShelvesPartial")
    suspend fun createSTDetailWithUserAndShelvesPartial(
        @Query("stHeaderId") stHeaderId: Int,
        @Query("assignedUserId") assignedUserId: Int,
        @Query("shelfId") shelfId: Int
    ): ResultModel<IdResponse>

    @POST("api/services/app/StockTaking/FinishPartialStockTacking")
    suspend fun finishPartialStockTacking(
        @Query("stHeaderId") stHeaderId:Int
    ) : BaseResponse

    @POST("api/services/app/StockTaking/NextPartialStockTackingDetail")
    suspend fun nextPartialStockTackingDetail(
        @Query("stDetailId") stDetailId:Int
    ) : BaseResponse

    @POST("api/services/app/StockTaking/CreateSTActionProcessFromPartialStockTaking")
    suspend fun createSTActionProcessFromPartialStockTaking(
        @Query("stHeaderId") stHeaderId:Int,
        @Query("stDetailId") stDetailId:Int,
        @Query("ProductId") productId:Int,
        @Query("newShelfCurrentQuantity") newShelfCurrentQuantity:Int,
    ) : BaseResponse

}