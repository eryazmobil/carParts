package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class OrderDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("orderHeader")
    val orderHeader: OrderHeaderResponse?,
    @SerializedName("product")
    val product: ProductResponse,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("quantityShipped")
    val quantityShipped: Int,
    @SerializedName("quantityCollected")
    val quantityCollected: Int,
    @SerializedName("quantityReplacement")
    val quantityReplacement: Int
)