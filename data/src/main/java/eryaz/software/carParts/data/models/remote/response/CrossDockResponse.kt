package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class CrossDockResponse(
    @SerializedName("id")
    val id:Int,

    @SerializedName("orderDetail")
    val orderDetail: OrderDetailResponse,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("isFinished")
    val isFinished: Boolean,

    @SerializedName("quantityNeed")
    val quantityNeed: Int,
)