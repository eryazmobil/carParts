package eryaz.software.carParts.data.repositories

import eryaz.software.carParts.data.api.services.OrderService
import eryaz.software.carParts.data.api.utils.ResponseHandler
import eryaz.software.carParts.data.mappers.toDto

class OrderRepo(private val api: OrderService) : BaseRepo() {

    suspend fun fetchCrossDockList(companyId: Int, warehouseId: Int) = callApi {
        val response = api.getReportCrossDockRequestList(companyId, warehouseId)
        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getOrderDetailPickingList(workActivityId: Int, userId: Int) = callApi {
        val response = api.getOrderDetailPickingList(workActivityId, userId)
        ResponseHandler.handleSuccess(response, response.result.toDto())
    }

    suspend fun createStockOut(productId: Int, shelfId: Int) = callApi {
        val response = api.createStockOut(productId = productId, shelfId = shelfId)
        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun updateOrderDetailCollectedAddQuantityForPda(
        productId: Int,
        workActionId: Int,
        shelfId: Int,
        containerId: Int,
        quantity: Int,
        orderDetailId: Int,
        fifoCode: String,
    ) = callApi {
        val response = api.updateOrderDetailCollectedAddQuantityForPda(
            workActionId = workActionId,
            productId = productId,
            shelfId = shelfId,
            containerId = containerId,
            quantity = quantity,
            orderDetailId = orderDetailId,
            fifoCode = fifoCode
        )
        ResponseHandler.handleSuccess(response, response.success)
    }


    suspend fun getOrderDetailListForPda(workActivityId: Int) = callApi {
        val response = api.getOrderDetailListForPda(workActivityId = workActivityId)

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun checkCrossDockNeedByActionId(workActionId: Int) = callApi {
        val response = api.checkCrossDockNeedByActionId(workActionId = workActionId)

        ResponseHandler.handleSuccess(response, response.result)
    }

    suspend fun createCrossDockRequest(workActionId: Int) = callApi {
        val response = api.createCrossDockRequest(workActionId = workActionId)

        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun getControlPointList(warehouseId: Int, type: Int) = callApi {
        val response = api.getControlPointList(warehouseId = warehouseId, type = type)

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getOrderHeaderListByControlPointId(controlPointId: Int) = callApi {
        val response =
            api.getOrderHeaderListByControlPointId(controlPointId = controlPointId)

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getOrderDetailList(headerId: Int) = callApi {
        val response =
            api.getOrderDetailList(headerId = headerId)

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun addQuantityForControl(
        orderHeaderId: Int,
        quantity: Int,
        productId: Int,
        isControlDoubleClick: Boolean,
        isPackage: Boolean,
        packageId: Int
    ) = callApi {
        val response =
            api.addQuantityForControl(
                orderHeaderId = orderHeaderId,
                quantity = quantity,
                productId = productId,
                isControlDoubleClick = isControlDoubleClick,
                isPackage = isPackage,
                packageId = packageId
            )

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getPackageList(orderHeaderId: Int) = callApi {
        val response =
            api.getPackageList(orderHeaderId = orderHeaderId)

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun createPackage(
        orderHeaderId: Int,
        sizeHeight: Double,
        sizeLength: Double,
        sizeWidth: Double,
        deci: Double,
        tareWeight: Double,
        packageNo: Int
    ) = callApi {
        val response = api.createPackage(
            orderHeaderId = orderHeaderId,
            sizeHeight = sizeHeight,
            sizeLength = sizeLength,
            sizeWidth = sizeWidth,
            deci = deci,
            tareWeight = tareWeight,
            packageNo = packageNo,
        )

        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun updatePackage(
        packageId: Int,
        sizeHeight: Double,
        sizeLength: Double,
        sizeWidth: Double,
        deci: Double,
        tareWeight: Double,
        isLocked: Boolean
    ) = callApi {
        val response = api.updatePackage(
            orderHeaderId = packageId,
            sizeHeight = sizeHeight,
            sizeLength = sizeLength,
            sizeWidth = sizeWidth,
            deci = deci,
            tareWeight = tareWeight,
            isLocked = isLocked,
        )

        ResponseHandler.handleSuccess(response, response.success)
    }

    suspend fun getRouteList(
        warehouseId: Int
    ) = callApi {
        val response = api.getRouteList(warehouseId = warehouseId)

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun createOrderHeaderRoute(
        code: String,
        shippingRouteId: Int,
        routeType: Int
    ) = callApi {
        val response = api.createOrderHeaderRoute(
            code = code,
            shippingRouteId = shippingRouteId,
            routeType = routeType
        )

        ResponseHandler.handleSuccess(response,response.success)
    }

    suspend fun updateOrderHeaderRoute(
        code: String,
        shippingRouteId: Int,
        deliveredPerson: String,
    ) = callApi {
        val response = api.updateOrderHeaderRoute(
            code = code,
            deliveredPerson = deliveredPerson,
            shippingRouteId = shippingRouteId,
        )

        ResponseHandler.handleSuccess(response,response.success)
    }

    suspend fun getOrderHeaderRouteList(
        shippingRouteId: Int
    ) = callApi {
        val response = api.getOrderHeaderRouteList(shippingRouteId = shippingRouteId)

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getOrderHeaderRouteDetailList(
        shippingRouteId: Int,
        orderHeaderId :Int
    ) = callApi {
        val response = api.getOrderHeaderRouteDetailList(
            shippingRouteId = shippingRouteId,
            orderHeaderId = orderHeaderId)

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }

    suspend fun getOrderDetailForRoute(
        orderHeaderId :Int
    ) = callApi {
        val response = api.getOrderDetailForRoute(
            orderHeaderId = orderHeaderId)

        ResponseHandler.handleSuccess(response, response.result.map { it.toDto() })
    }
}