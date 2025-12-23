package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class StockTackingProcessResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("stockTakingDetail")
    val stockTakingDetail: StockTakingDetailResponse?,
    @SerializedName("stShelf")
    val shelf:ShelfResponse?,
    @SerializedName("shelfCurrentQuantity")
    val shelfCurrentQuantity:Double?,
    @SerializedName("product")
    val product:ProductResponse?
)