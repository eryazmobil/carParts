package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class CrossDockCheckResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("orderHeader")
    val orderHeader: OrderHeaderResponse
)
