package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class ShippingTypeResponse(
    @SerializedName("code")
    val code: String?,
   @SerializedName("shortCode")
    val shortCode: String?,
    @SerializedName("definition")
    val definition: String?,
    @SerializedName("id")
    val id: Int
)