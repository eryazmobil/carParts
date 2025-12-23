package eryaz.software.carParts.data.api.services

import eryaz.software.carParts.data.models.dto.BarcodeDto
import eryaz.software.carParts.data.models.remote.models.ResultModel
import eryaz.software.carParts.data.models.remote.request.BarcodeRequestModel
import eryaz.software.carParts.data.models.remote.response.ProductResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BarcodeService {

    @GET("api/services/app/Product/GetProductByCode")
    suspend fun getProductByCode(
        @Query("companyId") companyId: Int,
        @Query("code") code: String
    ): ResultModel<ProductResponse>

    @GET("api/services/app/Product/GetProductListSearchLimited")
    suspend fun getProductList(
        @Query("companyId") companyId: Int,
        @Query("searchText") searchText: String,
        @Query("limit") limit: Int,
    ): ResultModel<List<ProductResponse>>

    @POST("api/services/app/Product/CreateBarcode")
    suspend fun createBarcode(
        @Body barcodeResponse: BarcodeRequestModel
    ) : ResultModel<BarcodeDto>
}