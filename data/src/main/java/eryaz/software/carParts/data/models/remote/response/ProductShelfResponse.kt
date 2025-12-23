package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class ProductShelfResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("product")
    val product: ProductResponse,

    @SerializedName("shelf")
    val shelf: ShelfResponse,

    @SerializedName("safetyPercent")
    val safetyPercent: Int,

    @SerializedName("maxQuantity")
    val maxQuantity: Int,

    @SerializedName("quantity")
    val quantity: Int
)