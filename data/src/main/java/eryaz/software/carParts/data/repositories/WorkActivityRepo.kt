package eryaz.software.carParts.data.repositories

import eryaz.software.carParts.data.api.services.WorkActivityService
import eryaz.software.carParts.data.api.utils.ResponseHandler
import eryaz.software.carParts.data.mappers.toDto
import eryaz.software.carParts.data.models.remote.request.ProductShelfInsertRequest
import eryaz.software.carParts.data.models.remote.request.ProductShelfUpdateRequest

class WorkActivityRepo(private val api: WorkActivityService) : BaseRepo() {

    suspend fun getWaybillWorkActivityList(companyId: Int, warehouseId: Int) = callApi {
        val response = api.getWaybillWorkActivityList(companyId, warehouseId)
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getTransferRequestReceivingWorkActivityList(companyId: Int, warehouseId: Int) =
        callApi {
            val response =
                api.getTransferRequestReceivingWorkActivityListForPdaControl(companyId, warehouseId)
            ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
        }

    suspend fun getWaybillListDetail(workActivityId: Int) = callApi {
        val response = api.getWaybillListDetail(activityId = workActivityId)
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getWorkActionForPda(userId: Int, workActivityId: Int, actionTypeId: Int) = callApi {
        val response = api.getWorkActionForPda(userId, workActivityId, actionTypeId)
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun createWorkAction(activityId: Int, actionTypeCode: String) = callApi {
        val response = api.createWorkAction(activityId, actionTypeCode)
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun finishWorkAction(actionId: Int) = callApi {
        val response = api.finishWorkAction(actionId)
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun getBarcodeByCode(code: String, companyId: Int) = callApi {
        val response = api.getBarcodeByCode(code, companyId)
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun getShelfByCode(code: String, warehouseId: Int, storageId: Int) = callApi {
        val response = api.getShelfByCode(
            code = code, warehouseId = warehouseId, storageId = storageId
        )
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun getShelfByCodeForStocktaking(
        code: String,
        warehouseId: Int,
        storageId: Int,
        companyId: Int
    ) = callApi {
        val response = api.getShelfByCodeForStocktaking(
            code = code, warehouseId = warehouseId, storageId = storageId, companyId = companyId
        )
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun updateWaybillControlAddQuantity(
        actionId: Int,
        productId: Int,
        quantity: Int,
        allowOverload: Boolean,
        serialLot: String,
        containerCount: Int
    ) = callApi {
        val response = api.updateWaybillControlAddQuantity(
            actionId = actionId,
            productId = productId,
            quantity = quantity,
            allowOverload = allowOverload,
            serialLot = serialLot,
            containerCount = containerCount
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun updateTransferRequestControlAddQuantity(
        actionId: Int,
        productId: Int,
        quantity: Int,
        allowOverload: Boolean,
        serialLot: String,
        containerCount: Int
    ) = callApi {
        val response = api.updateTransferRequestControlAddQuantity(
            actionId = actionId,
            productId = productId,
            quantity = quantity,
            allowOverload = allowOverload,
            serialLot = serialLot,
            containerCount = containerCount
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun updateWaybillPlacementAddQuantity(
        actionId: Int,
        productId: Int,
        quantity: Int,
        shelfId: Int,
        containerId: Int,
        crossDockId: Int,
        note: String
    ) = callApi {
        val response = api.updateWaybillPlacementAddQuantity(
            actionId = actionId,
            productId = productId,
            quantity = quantity,
            shelfId = shelfId,
            containerId = containerId,
            crossDockId = crossDockId,
            note = note
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun updateTransferRequestPlacementAddQuantity(
        actionId: Int,
        productId: Int,
        quantity: Int,
        shelfId: Int,
        containerId: Int,
        crossDockId: Int
    ) = callApi {
        val response = api.updateTransferRequestPlacementAddQuantity(
            actionId = actionId,
            productId = productId,
            quantity = quantity,
            shelfId = shelfId,
            containerId = containerId,
            crossDockId = crossDockId
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun getCrossDockRequestForPlacing(
        productId: Int, companyId: Int, warehouseId: Int
    ) = callApi {
        val response = api.getCrossDockRequestForPlacing(
            productId = productId, companyId = companyId, warehouseId = warehouseId
        )
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun getShelfListForPlacement(
        productId: Int, includeOld: Boolean, actionId: Int
    ) = callApi {
        val response = api.getShelfListForPlacement(
            productId = productId, actionId = actionId, includeOld = includeOld
        )
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun createProductShelfInsert(
        productShelfInsert: ProductShelfInsertRequest
    ) = callApi {
        val response = api.createProductShelfInsert(
            productShelfInsert = productShelfInsert
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun updateProductShelf(
        productShelfUpdate: ProductShelfUpdateRequest
    ) = callApi {
        val response = api.updateProductShelf(
            productShelfInsert = productShelfUpdate
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun deleteProductShelf(
        productId: Int
    ) = callApi {
        val response = api.deleteProductShelf(
            id = productId
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun getWaybillDetailQuantityWaitingForPlacement(
        actionId: Int, productId: Int
    ) = callApi {
        val response = api.getWaybillDetailQuantityWaitingForPlacement(
            actionId = actionId, productId = productId
        )
        ResponseHandler.handleSuccess(response, response.result)
    }

    suspend fun getProductVarietyShelf(
        productId: Int
    ) = callApi {
        val response = api.getProductVarietyShelf(
            productId = productId
        )
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun createStorageMovement(
        productId: Int,
        enterStorageId: Int,
        exitStorageId: Int,
        enterShelfId: Int,
        exitShelfId: Int,
        quantity: Int,
        warehouseId: Int
    ) = callApi {
        val response = api.createStorageMovement(
            productId = productId,
            enterStorageId = enterStorageId,
            exitStorageId = exitStorageId,
            enterShelfId = enterShelfId,
            exitShelfId = exitShelfId,
            quantity = quantity,
            warehouseId = warehouseId
        )
        ResponseHandler.handleSuccess(response, response)
    }

    suspend fun createShelfMovement(
        productId: Int, enterShelfId: Int, exitShelfId: Int, quantity: Int, changeVariety: Boolean
    ) = callApi {
        val response = api.createShelfMovement(
            productId = productId,
            enterShelfId = enterShelfId,
            exitShelfId = exitShelfId,
            quantity = quantity,
            changeVariety = changeVariety
        )
        ResponseHandler.handleSuccess(response, response)
    }

    suspend fun transferAllShelfMovementForPda(
        companyId: Int,
        warehouseId: Int,
        exitShelfId: Int,
        enterShelfId: Int,
        changeVariety: Boolean
    ) = callApi {
        val response = api.transferAllShelfMovementForPda(
            companyId = companyId,
            warehouseId = warehouseId,
            exitShelfId = exitShelfId,
            enterShelfId = enterShelfId,
            changeVariety = changeVariety
        )
        ResponseHandler.handleSuccess(response, response)
    }

    suspend fun createStockCorrection(
        type: Int,
        isProcessToErp: Boolean,
        storageId: Int,
        shelfId: Int,
        productId: Int,
        quantity: Double,
        price: Double,
        notes: String
    ) = callApi {
        val response = api.createStockCorrection(
            type = type,
            isProcessToErp = isProcessToErp,
            storageId = storageId,
            shelfId = shelfId,
            productId = productId,
            quantity = quantity,
            price = price,
            notes = notes
        )
        ResponseHandler.handleSuccess(response, response)
    }

    suspend fun getProductStorageQuantityList(
        productId: Int,
        warehouseId: Int,
        storageId: Int,
        companyId: Int
    ) = callApi {
        val response = api.getProductStorageQuantityList(
            productId = productId,
            companyId = companyId,
            warehouseId = warehouseId,
            storageId = storageId
        )
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getProductShelfQuantityList(
        productId: Int,
        warehouseId: Int,
        storageId: Int,
        companyId: Int,
        shelfId: Int
    ) = callApi {
        val response = api.getProductShelfQuantityList(
            productId = productId,
            companyId = companyId,
            warehouseId = warehouseId,
            storageId = storageId,
            shelfId = shelfId
        )
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getProductVarietyShelfListByShelfId(
        shelfId: Int
    ) = callApi {
        val response = api.getProductVarietyShelfListByShelfId(shelfId = shelfId)

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getWorkActionActive(
        companyId: Int,
        warehouseId: Int,
        workActivityType: String,
        workActionType: String,
    ) = callApi {
        val response = api.getWorkActionActiveForPda(
            companyId = companyId,
            warehouseId = warehouseId,
            workActionType = workActionType,
            workActivityType = workActivityType
        )
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun getShippingWorkActivityList(
        companyId: Int,
        warehouseId: Int,
    ) = callApi {
        val response = api.getShippingWorkActivityList(
            companyId = companyId,
            warehouseId = warehouseId
        )
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getTransferWorkActivityList(
        companyId: Int,
        warehouseId: Int,
    ) = callApi {
        val response = api.getTransferWorkActivityList(
            companyId = companyId,
            warehouseId = warehouseId
        )
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getTransferDetailPickingList(
        workActivityId: Int
    ) = callApi {
        val response = api.getTransferDetailPickingList(
            workActivityId = workActivityId
        )
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun getTransferRequestDetailList(
        workActivityId: Int
    ) = callApi {
        val response = api.getTransferRequestDetailList(
            workActivityId = workActivityId
        )
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getTransferWarehouseWorkActivityListForPdaControl(
        companyId: Int,
        warehouseId: Int
    ) = callApi {
        val response = api.getTransferWarehouseWorkActivityListForPdaControl(
            companyId = companyId,
            warehouseId = warehouseId
        )
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun updateTransferRequestDetailPickedAddQuantity(
        productId: Int,
        workActionId: Int,
        shelfId: Int,
        containerId: Int,
        quantity: Int,
        transferRequestDetailId: Int,
    ) = callApi {
        val response = api.updateTransferRequestDetailPickedAddQuantity(
            workActionId = workActionId,
            productId = productId,
            shelfId = shelfId,
            containerId = containerId,
            quantity = quantity,
            transferRequestDetailId = transferRequestDetailId
        )
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun getTransferDetailControlListForPda(
        workActivityId: Int
    ) = callApi {
        val response = api.getTransferDetailControlListForPda(workActivityId = workActivityId)

        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun addQuantityForTransferRequestDetailControl(
        transferHeaderId: Int,
        quantity: Int,
        productId: Int
    ) = callApi {
        val response = api.addQuantityForTransferRequestDetailControl(
            transferHeaderId = transferHeaderId,
            productId = productId,
            quantity = quantity
        )

        ResponseHandler.handleSuccess(response, response)
    }

    suspend fun getControlPointForProductByQuantity(
        companyId: Int,
        warehouseId: Int,
        productId: Int
    ) = callApi {
        val response = api.getControlPointForProductByQuantity(
            companyId = companyId,
            warehouseId = warehouseId,
            productId = productId
        )

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getPackageDetailListByProductId(
        productId: Int
    ) = callApi {
        val response = api.getPackageDetailListByProductId(
            productId = productId
        )

        ResponseHandler.handleSuccess(response, response.result)
    }
}