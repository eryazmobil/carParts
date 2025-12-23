package eryaz.software.carParts.data.models.dto

data class VehiclePackageDto(

    val id: Int,
    val code: String,
    val clientName: String,
    val orderHeaderId: Int,
    val quantityPackTotal: Int,
    val quantityLoadPack: Int,
    val quantityUnLoadPack: Int,
    val shippingRoute: ShippingRouteDto
)