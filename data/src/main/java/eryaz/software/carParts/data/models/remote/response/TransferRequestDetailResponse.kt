package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class TransferRequestDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("product")
    val product: ProductResponse,
    @SerializedName("quantity")
    val quantity: Double,
    @SerializedName("quantityPicked")
    val quantityPicked: Double,
    @SerializedName("quantityShipped")
    val quantityShipped: Double
)