package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class WarehouseResponse(

    @SerializedName("code")
    val code: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("id")
    val id: Int,
)