package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class BarcodeResponse(

    @SerializedName("id")
    val id: Int,
    @SerializedName("product")
    val product: ProductResponse,
    @SerializedName("code")
    val code: String,
    @SerializedName("quantity")
    val quantity: Int
)