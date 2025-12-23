package eryaz.software.carParts.data.repositories

import eryaz.software.carParts.data.api.services.CountingService
import eryaz.software.carParts.data.api.utils.ResponseHandler
import eryaz.software.carParts.data.mappers.toDto
import eryaz.software.carParts.data.models.remote.request.FastCountingProcessRequestModel

class CountingRepo(private val api: CountingService) : BaseRepo() {

    suspend fun getCountingWorkActivityList(companyId: Int, warehouseId: Int) = callApi {
        val response = api.getCountingWorkActivityList(companyId, warehouseId)
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getSTActionProcessListForShelf(stHeaderId: Int, assignedShelfId: Int) = callApi {
        val response = api.getSTActionProcessListForShelf(stHeaderId, assignedShelfId)
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getShelfIsOnAssignedUser(stHeaderId: Int, userId: Int, shelfId: Int) = callApi {
        val response = api.getShelfIsOnAssignedUser(stHeaderId, userId, shelfId)
        ResponseHandler.handleSuccess(response, response.result)
    }

    suspend fun createSTActionProcessFromSTDetail(
        stHeaderId: Int,
        userId: Int,
        assignedShelfId: Int
    ) = callApi {
        val response = api.createSTActionProcessFromSTDetail(
            stHeaderId = stHeaderId,
            processUserId = userId,
            assignedShelfId = assignedShelfId
        )
        ResponseHandler.handleSuccess(response, response)
    }

    suspend fun finishSTDetail(stDetailId: Int, userId: Int) = callApi {
        val response = api.finishSTDetail(stDetailId, userId)
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun addProduct(
        stHeaderId: Int,
        userId: Int,
        assignedShelfId: Int,
        productId: Int,
        countedQuantity: Int
    ) = callApi {
        val response = api.addProduct(
            stHeaderId = stHeaderId,
            userId = userId,
            assignedShelfId = assignedShelfId,
            productId = productId,
            countedQuantity = countedQuantity
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun updateQuantitySTActionProcess(
        stActionProcessId: Int,
        assignedShelfId: Int,
        productId: Int,
        countedQuantity: Int
    ) = callApi {
        val response = api.updateQuantitySTActionProcess(
            stActionProcessId = stActionProcessId,
            assignedShelfId = assignedShelfId,
            productId = productId,
            countedQuantity = countedQuantity
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun deleteSTActionProcess(
        stActionProcessId: Int,
    ) = callApi {
        val response = api.deleteSTActionProcess(
            stActionProcessId = stActionProcessId,
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun getAllAssignedToUser(
        stActionProcessId: Int,
        userId: Int
    ) = callApi {
        val response = api.getAllAssignedToUser(
            stActionProcessId = stActionProcessId,
            processUserId = userId
        )
        ResponseHandler.handleSuccess(response, response.result)
    }

    suspend fun isSuitableShelf(
        stHeaderId: Int,
        shelfId: Int
    ) = callApi {
        val response = api.isSuitableShelf(
            stHeaderId = stHeaderId,
            shelfId = shelfId
        )
        ResponseHandler.handleSuccess(response, response.result)
    }

    suspend fun getAllAssignedDetailsToUser(
        stHeaderId: Int,
        shelfId: Int,
        userId: Int
    ) = callApi {
        val response = api.getAllAssignedDetailsToUser(
            stHeaderId = stHeaderId,
            shelfId = shelfId,
            userId = userId
        )
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getProductQuantityListWithShelf(
        stHeaderId: Int,
        shelfId: Int,
        productId: Int,
        companyId: Int,
        warehouseId: Int
    ) = callApi {
        val response = api.getProductQuantityListWithShelf(
            stHeaderId = stHeaderId,
            shelfId = shelfId,
            productId = productId,
            companyId = companyId,
            warehouseId = warehouseId
        )
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun finishFastStocktakingDetail(
        fastCountingProcessRequest: FastCountingProcessRequestModel
    ) = callApi {
        val response = api.finishFastStocktakingDetail(
            fastCountingProcessRequest = fastCountingProcessRequest
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun getPdaPartialStockTakingWorkActivityAndSTHeader(
        companyId: Int,
        warehouseId: Int
    ) = callApi {
        val response = api.getPdaPartialStockTakingWorkActivityAndSTHeader(
            companyId,
            warehouseId
        )
        ResponseHandler.handleSuccess(response, response.result.id)
    }

    suspend fun createSTDetailWithUserAndShelvesPartial(
        stHeaderId: Int,
        assignedUserId: Int,
        shelfId: Int
    ) = callApi {
        val response = api.createSTDetailWithUserAndShelvesPartial(
            stHeaderId,
            assignedUserId,
            shelfId
        )
        ResponseHandler.handleSuccess(response, response.result.id)
    }

    suspend fun finishPartialStockTacking(
        stHeaderId: Int
    ) = callApi {
        val response = api.finishPartialStockTacking(stHeaderId)
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun nextPartialStockTackingDetail(
        stDetailId: Int
    ) = callApi {
        val response = api.nextPartialStockTackingDetail(stDetailId)
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun createSTActionProcessFromPartialStockTaking(
        stHeaderId: Int,
        stDetailId: Int,
        productId: Int,
        newShelfCurrentQuantity: Int
    ) = callApi {
        val response = api.createSTActionProcessFromPartialStockTaking(
            stHeaderId, stDetailId, productId, newShelfCurrentQuantity
        )
        ResponseHandler.handleSuccess(response, response.success)
    }
}