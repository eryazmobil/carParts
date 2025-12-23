package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("code")
    val code: Int,

    @SerializedName("message")
    val message: String
)