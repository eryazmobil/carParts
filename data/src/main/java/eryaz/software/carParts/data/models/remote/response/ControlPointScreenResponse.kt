package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class ControlPointScreenResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("code")
    val code: String,
    @SerializedName("definition")
    val definition: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("pickerUsers")
    val pickerUsers: String,
    @SerializedName("shippingTypes")
    val shippingTypes: String,
    @SerializedName("clientNames")
    val clientNames: String
)
