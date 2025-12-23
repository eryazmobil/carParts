package eryaz.software.carParts.data.repositories

import eryaz.software.carParts.data.api.services.BarcodeService
import eryaz.software.carParts.data.api.utils.ResponseHandler
import eryaz.software.carParts.data.mappers.toDto
import eryaz.software.carParts.data.models.remote.request.BarcodeRequestModel

class BarcodeRepo(private val api: BarcodeService) : BaseRepo() {

    suspend fun getProductByCode(companyId: Int, code: String) = callApi {
        val response = api.getProductByCode(companyId, code)
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun createBarcode(barcodeRequestModel: BarcodeRequestModel) = callApi {
        val response = api.createBarcode(barcodeRequestModel)
        ResponseHandler.handleSuccess(response, response.result)
    }

    suspend fun getProductList(
        companyId: Int,
        searchTxt: String,
        limit: Int
    ) = callApi {
        val response = api.getProductList(
            companyId = companyId,
            searchText = searchTxt,
            limit = limit
        )
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }
}