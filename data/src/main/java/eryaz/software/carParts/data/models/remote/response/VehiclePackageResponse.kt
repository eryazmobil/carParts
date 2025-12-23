package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class VehiclePackageResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("documentNo")
    val code: String,

    @SerializedName("clientName")
    val clientName: String,

    @SerializedName("orderHeaderId")
    val orderHeaderId: Int,

    @SerializedName("quantityPackTotal")
    val quantityPackTotal: Int,

    @SerializedName("quantityLoadPack")
    val quantityLoadPack: Int,

    @SerializedName("quantityUnLoadPack")
    val quantityUnLoadPack: Int,

    @SerializedName("shippingRoute")
    val shippingRoute: ShippingRouteResponse
)