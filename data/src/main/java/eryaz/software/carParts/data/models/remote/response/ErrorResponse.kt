package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val error: ErrorModel
)