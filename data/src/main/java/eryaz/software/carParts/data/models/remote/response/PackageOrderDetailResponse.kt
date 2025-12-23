package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class PackageOrderDetailResponse(

    @SerializedName("id")
    val id: Int,
    @SerializedName("product")
    val product: ProductResponse,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("quantityShipped")
    val quantityShipped: Int,
    @SerializedName("quantityCollected")
    val quantityCollected: Int
)