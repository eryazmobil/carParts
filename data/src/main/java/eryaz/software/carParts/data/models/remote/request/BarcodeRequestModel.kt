package eryaz.software.carParts.data.models.remote.request

import com.google.gson.annotations.SerializedName

data class BarcodeRequestModel(
    @SerializedName("productId")
    val productId: Int,

    @SerializedName("companyId")
    val companyId: Int,

    @SerializedName("code")
    val code: String,

    @SerializedName("quantity")
    val quantity: Int
)