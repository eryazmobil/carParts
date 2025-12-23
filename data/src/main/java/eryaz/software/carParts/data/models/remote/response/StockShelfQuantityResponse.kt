package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class StockShelfQuantityResponse(
    @SerializedName("shelf")
    val shelf: ShelfResponse,
    @SerializedName("quantity")
    val quantity: Double
    )
