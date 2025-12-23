package eryaz.software.carParts.data.api.services

import eryaz.software.carParts.data.models.remote.models.ResultModel
import eryaz.software.carParts.data.models.remote.request.ProductShelfInsertRequest
import eryaz.software.carParts.data.models.remote.request.ProductShelfUpdateRequest
import eryaz.software.carParts.data.models.remote.response.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface WorkActivityService {

    @GET("api/services/app/Work/GetReceivingWorkActivityListForPdaControl")
    suspend fun getWaybillWorkActivityList(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int
    ): ResultModel<List<WorkActivityResponse>>

    @GET("api/services/app/Work/GetTransferRequestReceivingWorkActivityListForPdaControl")
    suspend fun getTransferRequestReceivingWorkActivityListForPdaControl(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int
    ): ResultModel<List<WorkActivityResponse>>

    @GET("api/services/app/Waybill/GetWaybillDetailListForPdaReceiving")
    suspend fun getWaybillListDetail(
        @Query("activityId") activityId: Int
    ): ResultModel<List<WaybillListDetailResponse>>

    @GET("api/services/app/Work/GetWorkActionForPda")
    suspend fun getWorkActionForPda(
        @Query("userId") userId: Int,
        @Query("workActivityId") workActivityId: Int,
        @Query("actionTypeId") actionTypeId: Int
    ): ResultModel<WorkActionResponse>

    @POST("api/services/app/Work/CreateWorkAction")
    suspend fun createWorkAction(
        @Query("activityId") activityId: Int,
        @Query("actionTypeCode") actionTypeCode: String,
    ): ResultModel<WorkActionResponse>

    @POST("api/services/app/Work/FinishWorkAction")
    suspend fun finishWorkAction(
        @Query("actionId") actionId: Int
    ): BaseResponse

    @GET("api/services/app/Product/GetBarcodeByCode")
    suspend fun getBarcodeByCode(
        @Query("code") code: String,
        @Query("companyId") companyId: Int
    ): ResultModel<BarcodeResponse>

    @GET("api/services/app/Product/GetShelfByCode")
    suspend fun getShelfByCode(
        @Query("code") code: String,
        @Query("warehouseId") warehouseId: Int,
        @Query("storageId") storageId: Int
    ): ResultModel<ShelfResponse>

    @GET("api/services/app/Product/GetShelfByCodeForStocktaking")
    suspend fun getShelfByCodeForStocktaking(
        @Query("code") code: String,
        @Query("warehouseId") warehouseId: Int,
        @Query("companyId") companyId: Int,
        @Query("storageId") storageId: Int
    ): ResultModel<ShelfResponse>

    @PUT("api/services/app/Waybill/UpdateWaybillControlAddQuantity")
    suspend fun updateWaybillControlAddQuantity(
        @Query("actionId") actionId: Int,
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Int,
        @Query("allowOverload") allowOverload: Boolean,
        @Query("serialLot") serialLot: String,
        @Query("containerCount") containerCount: Int
    ): BaseResponse

    @PUT("api/services/app/Work/UpdateTransferRequestControlAddQuantity")
    suspend fun updateTransferRequestControlAddQuantity(
        @Query("actionId") actionId: Int,
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Int,
        @Query("allowOverload") allowOverload: Boolean,
        @Query("serialLot") serialLot: String,
        @Query("containerCount") containerCount: Int
    ): BaseResponse

    @PUT("api/services/app/Waybill/UpdateWaybillPlacementAddQuantity")
    suspend fun updateWaybillPlacementAddQuantity(
        @Query("actionId") actionId: Int,
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Int,
        @Query("shelfId") shelfId: Int,
        @Query("containerId") containerId: Int,
        @Query("crossDockId") crossDockId: Int
    ): BaseResponse

    @PUT("api/services/app/Work/UpdateTransferRequestPlacementAddQuantity")
    suspend fun updateTransferRequestPlacementAddQuantity(
        @Query("actionId") actionId: Int,
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Int,
        @Query("shelfId") shelfId: Int,
        @Query("containerId") containerId: Int,
        @Query("crossDockId") crossDockId: Int
    ): BaseResponse

    @GET("api/services/app/Order/GetCrossDockRequestForPlacing")
    suspend fun getCrossDockRequestForPlacing(
        @Query("productId") productId: Int,
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int
    ): ResultModel<CrossDockCheckResponse>

    @GET("api/services/app/Product/GetShelfListForPlacementForPda")
    suspend fun getShelfListForPlacement(
        @Query("productId") productId: Int,
        @Query("includeOld") includeOld: Boolean,
        @Query("actionId") actionId: Int
    ): ResultModel<List<ShelfResponse>>

    @GET("api/services/app/Waybill/GetWaybillDetailQuantityWaitingForPlacement")
    suspend fun getWaybillDetailQuantityWaitingForPlacement(
        @Query("actionId") actionId: Int,
        @Query("productId") productId: Int
    ): ResultModel<Int>

    @POST("api/services/app/Work/CreateStorageMovement")
    suspend fun createStorageMovement(
        @Query("productId") productId: Int,
        @Query("storageIdTo") enterStorageId: Int,
        @Query("StorageIdFrom") exitStorageId: Int,
        @Query("shelfIdTo") enterShelfId: Int,
        @Query("shelfIdFrom") exitShelfId: Int,
        @Query("quantity") quantity: Int,
        @Query("warehouseId") warehouseId: Int
    ): BaseResponse

    @POST("api/services/app/Work/CreateShelfMovement")
    suspend fun createShelfMovement(
        @Query("productId") productId: Int,
        @Query("shelfIdTo") enterShelfId: Int,
        @Query("shelfIdFrom") exitShelfId: Int,
        @Query("quantity") quantity: Int,
        @Query("changeVariety") changeVariety: Boolean
    ): BaseResponse

    @POST("api/services/app/Work/CreateBatchShelfMovementForPda")
    suspend fun transferAllShelfMovementForPda(
        @Query("CompanyId") companyId: Int,
        @Query("WarehouseId") warehouseId: Int,
        @Query("oldShelf") exitShelfId: Int,
        @Query("newShelf") enterShelfId: Int,
        @Query("changeVariety") changeVariety: Boolean
    ): BaseResponse

    @POST("api/services/app/Work/CreateStockCorrection")
    suspend fun createStockCorrection(
        @Query("type") type: Int,
        @Query("isProcToErp") isProcessToErp: Boolean,
        @Query("storageId") storageId: Int,
        @Query("shelfId") shelfId: Int,
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Double,
        @Query("price") price: Double,
        @Query("notes") notes: String
    ): BaseResponse

    @POST("api/services/app/Product/CreateProductShelf")
    suspend fun createProductShelfInsert(
        @Body productShelfInsert: ProductShelfInsertRequest
    ): BaseResponse

    @POST("api/services/app/Product/UpdateProductShelf")
    suspend fun updateProductShelf(
        @Body productShelfInsert: ProductShelfUpdateRequest
    ): ResultModel<ProductShelfResponse>

    @DELETE("api/services/app/Product/DeleteProductShelf")
    suspend fun deleteProductShelf(
        @Query("id") id: Int
    ): ResultModel<ProductShelfResponse>

    @GET("api/services/app/Product/GetProductVarietyShelf")
    suspend fun getProductVarietyShelf(
        @Query("productId") productId: Int
    ): ResultModel<ProductShelfResponse>

    @GET("api/services/app/Product/getProductStorageQuantityList")
    suspend fun getProductStorageQuantityList(
        @Query("productId") productId: Int,
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int,
        @Query("storageId") storageId: Int
    ): ResultModel<List<ProductStorageQuantityResponse>>

    @GET("api/services/app/Product/GetProductShelfQuantityList")
    suspend fun getProductShelfQuantityList(
        @Query("productId") productId: Int,
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int,
        @Query("storageId") storageId: Int,
        @Query("shelfId") shelfId: Int
    ): ResultModel<List<ProductShelfQuantityResponse>>

    @GET("api/services/app/Product/GetProductVarietyShelfListByShelfId")
    suspend fun getProductVarietyShelfListByShelfId(
        @Query("shelfId") shelfId: Int
    ): ResultModel<List<ProductSpecialShelfResponse>>

    @GET("api/services/app/Work/GetWorkActionActiveForPda")
    suspend fun getWorkActionActiveForPda(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int,
        @Query("workActivityType") workActivityType: String,
        @Query("workActionType") workActionType: String
    ): ResultModel<WorkActionResponse>

    @GET("api/services/app/Work/GetShippingWorkActivityListForPdaPicking")
    suspend fun getShippingWorkActivityList(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int
    ): ResultModel<List<WorkActivityResponse>>

    @GET("api/services/app/Work/GetTransferWarehouseWorkActivityListForPdaPicking")
    suspend fun getTransferWorkActivityList(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int
    ): ResultModel<List<WorkActivityResponse>>

    @GET("api/services/app/Work/GetTransferDetailPickingListForPda")
    suspend fun getTransferDetailPickingList(@Query("workActivityId") workActivityId: Int)
            : ResultModel<TransferPickingResponse>

    @GET("api/services/app/Work/GetTransferRequestDetailListForPda")
    suspend fun getTransferRequestDetailList(@Query("workActivityId") workActivityId: Int)
            : ResultModel<List<TransferRequestDetailResponse>>

    @GET("api/services/app/Work/GetTransferDetailControlListForPda")
    suspend fun getTransferDetailControlListForPda(@Query("activityId") workActivityId: Int)
            : ResultModel<TransferRequestAllDetailResponse>

    @GET("api/services/app/Work/GetTransferWarehouseWorkActivityListForPdaControl")
    suspend fun getTransferWarehouseWorkActivityListForPdaControl(
        @Query("companyId") companyId: Int,
        @Query("warehouseId") warehouseId: Int
    ): ResultModel<List<WorkActivityResponse>>

    @PUT("api/services/app/Work/UpdateTransferRequestDetailPickedAddQuantityForPda")
    suspend fun updateTransferRequestDetailPickedAddQuantity(
        @Query("workActionId") workActionId: Int,
        @Query("productId") productId: Int,
        @Query("shelfId") shelfId: Int,
        @Query("containerId") containerId: Int,
        @Query("quantity") quantity: Int,
        @Query("transferRequestDetailId") transferRequestDetailId: Int,
    ): BaseResponse

    @POST("api/services/app/Work/AddQuantityForTransferRequestDetailControl")
    suspend fun addQuantityForTransferRequestDetailControl(
        @Query("transferHeaderId") transferHeaderId: Int,
        @Query("quantity") quantity: Int,
        @Query("productId") productId: Int
    ): BaseResponse

}