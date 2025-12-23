package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class IdResponse(
    @SerializedName("id")
    val id :Int
)