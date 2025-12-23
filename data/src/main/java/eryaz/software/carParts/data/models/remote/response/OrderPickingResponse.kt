package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class OrderPickingResponse(

    @SerializedName("orderDetailList")
    val orderDetailList: List<OrderDetailResponse>,
    @SerializedName("pickingSuggestionList")
    val pickingSuggestionList: List<PickingSuggestionResponse>,
)