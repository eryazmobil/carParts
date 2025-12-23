package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class TransferRequestHeaderResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("note")
    val note: String,
    @SerializedName("shippingType")
    val shippingType: String
)