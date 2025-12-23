package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class PickingSuggestionResponse(

    @SerializedName("sourceId")
    val id: Int,
    @SerializedName("product")
    val product: ProductResponse,
    @SerializedName("shelfForPicking")
    val shelfForPicking: StockShelfQuantityResponse,
    @SerializedName("quantityWillBePicked")
    val quantityWillBePicked: Int,
    @SerializedName("quantityPicked")
    val quantityPicked: Int,
    @SerializedName("controlPoint")
    val controlPoint: ControlPointResponse?,
    @SerializedName("collectPoint")
    val collectPoint: String?
)