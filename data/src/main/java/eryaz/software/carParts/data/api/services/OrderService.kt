package eryaz.software.carParts.data.api.services

import eryaz.software.carParts.data.models.remote.models.ResultModel
import eryaz.software.carParts.data.models.remote.response.BaseResponse
import eryaz.software.carParts.data.models.remote.response.ControlPointScreenResponse
import eryaz.software.carParts.data.models.remote.response.CrossDockResponse
import eryaz.software.carParts.data.models.remote.response.OrderDetailResponse
import eryaz.software.carParts.data.models.remote.response.OrderHeaderResponse
import eryaz.software.carParts.data.models.remote.response.OrderPickingResponse
import eryaz.software.carParts.data.models.remote.response.PackageOrderDetailResponse
import eryaz.software.carParts.data.models.remote.response.PackageResponse
import eryaz.software.carParts.data.models.remote.response.RouteResponse
import eryaz.software.carParts.data.models.remote.response.VehiclePackageResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface OrderService {

    @GET("api/services/app/Order/GetReportCrossDockRequestList")
    suspend fun getReportCrossDockRequestList(
        @Query("companyId") companyId: Int, @Query("warehouseId") warehouseId: Int
    ): ResultModel<List<CrossDockResponse>>

    @GET("api/services/app/Order/GetOrderDetailPickingListForPda")
    suspend fun getOrderDetailPickingList(
        @Query("workActivityId") workActivityId: Int, @Query("userId") userId: Int
    ): ResultModel<OrderPickingResponse>

    @POST("api/services/app/Product/CreateStockOut")
    suspend fun createStockOut(
        @Query("productId") productId: Int, @Query("shelfId") shelfId: Int
    ): BaseResponse

    @PUT("api/services/app/Order/UpdateOrderDetailCollectedAddQuantityForPda")
    suspend fun updateOrderDetailCollectedAddQuantityForPda(
        @Query("workActionId") workActionId: Int,
        @Query("productId") productId: Int,
        @Query("shelfId") shelfId: Int,
        @Query("containerId") containerId: Int,
        @Query("quantity") quantity: Int,
        @Query("orderdetailId") orderDetailId: Int,
        @Query("fifocode") fifoCode: String
    ): BaseResponse

    @GET("api/services/app/Order/GetOrderDetailListForPda")
    suspend fun getOrderDetailListForPda(
        @Query("workActivityId") workActivityId: Int
    ): ResultModel<List<OrderDetailResponse>>

    @POST("api/services/app/Order/CheckCrossDockNeedByActionId")
    suspend fun checkCrossDockNeedByActionId(
        @Query("workActionId") workActionId: Int
    ): ResultModel<Boolean>

    @POST("api/services/app/Order/CreateCrossDockRequestByWorkActionId")
    suspend fun createCrossDockRequest(
        @Query("workActionId") workActionId: Int
    ): BaseResponse

    @POST("api/services/app/Order/ControlPointScreen")
    suspend fun getControlPointList(
        @Query("warehouseId") warehouseId: Int, @Query("type") type: Int
    ): ResultModel<List<ControlPointScreenResponse>>

    @GET("api/services/app/Order/GetOrderHeaderListByControlPointId")
    suspend fun getOrderHeaderListByControlPointId(
        @Query("controlPointId") controlPointId: Int
    ): ResultModel<List<OrderHeaderResponse>>

    @GET("api/services/app/Order/GetOrderDetailList")
    suspend fun getOrderDetailList(
        @Query("headerId") headerId: Int
    ): ResultModel<List<OrderDetailResponse>>

    @POST("api/services/app/Order/AddQuantityForControl")
    suspend fun addQuantityForControl(
        @Query("orderHeaderId") orderHeaderId: Int,
        @Query("quantity") quantity: Int,
        @Query("productId") productId: Int,
        @Query("isControlDoubleClick") isControlDoubleClick: Boolean,
        @Query("isPackage") isPackage: Boolean,
        @Query("packageId") packageId: Int
    ): ResultModel<List<OrderDetailResponse>>

    @GET("api/services/app/Order/GetPackageList")
    suspend fun getPackageList(
        @Query("orderheaderId") orderHeaderId: Int
    ): ResultModel<List<PackageResponse>>

    @POST("api/services/app/Order/CreatePackage")
    suspend fun createPackage(
        @Query("orderheaderId") orderHeaderId: Int,
        @Query("sizeHeight") sizeHeight: Double,
        @Query("sizeLength") sizeLength: Double,
        @Query("sizeWidth") sizeWidth: Double,
        @Query("deci") deci: Double,
        @Query("tareWeight") tareWeight: Double,
        @Query("packageNo") packageNo: Int
    ): BaseResponse

    @PUT("api/services/app/Order/UpdatePackage")
    suspend fun updatePackage(
        @Query("packageId") orderHeaderId: Int,
        @Query("sizeHeight") sizeHeight: Double,
        @Query("sizeLength") sizeLength: Double,
        @Query("sizeWidth") sizeWidth: Double,
        @Query("deci") deci: Double,
        @Query("tareWeight") tareWeight: Double,
        @Query("isLocked") isLocked: Boolean
    ): BaseResponse

    @GET("api/services/app/Order/GetShippingRouteList")
    suspend fun getRouteList(
        @Query("warehouseId") warehouseId: Int
    ): ResultModel<List<RouteResponse>>

    @POST("api/services/app/Order/CreateOrderHeaderRoute")
    suspend fun createOrderHeaderRoute(
        @Query("code") code: String,
        @Query("shippingRouteId") shippingRouteId: Int,
        @Query("routeType") routeType: Int
    ): BaseResponse

    @PUT("api/services/app/Order/UpdateOrderHeaderRoute")
    suspend fun updateOrderHeaderRoute(
        @Query("code") code: String,
        @Query("shippingRouteId") shippingRouteId: Int,
        @Query("deliveredPerson") deliveredPerson: String
    ): BaseResponse

    @GET("api/services/app/Order/GetOrderHeaderRouteList")
    suspend fun getOrderHeaderRouteList(
        @Query("shippingRouteId") shippingRouteId: Int
    ): ResultModel<List<VehiclePackageResponse>>

    @GET("api/services/app/Order/GetOrderHeaderRouteDetailList")
    suspend fun getOrderHeaderRouteDetailList(
        @Query("shippingRouteId") shippingRouteId: Int,
        @Query("orderHeaderId") orderHeaderId: Int
    ): ResultModel<List<RouteResponse>>

    @GET("api/services/app/Order/GetOrderDetailForRoute")
    suspend fun getOrderDetailForRoute(
        @Query("orderHeaderId") orderHeaderId: Int
    ): ResultModel<List<PackageOrderDetailResponse>>
}