package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class WaybillListDetailResponse(

    @SerializedName("product")
    val product: ProductResponse,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("quantityOrder")
    val quantityOrder: Int,

    @SerializedName("quantityPlaced")
    val quantityPlaced: Int,

    @SerializedName("quantityControlled")
    val quantityControlled: Int,

    @SerializedName("placement")
    val placement: String?,

    @SerializedName("id")
    val id: Int
)