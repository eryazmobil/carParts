package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class RouteResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("code")
    val code: String
)