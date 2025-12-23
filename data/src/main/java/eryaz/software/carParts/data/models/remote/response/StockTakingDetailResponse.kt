package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class StockTakingDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("stockTakingHeader")
    val stockTakingHeader: StockTakingHeaderResponse?,
    @SerializedName("stockTakingType")
    val stockTakingType: WorkActivityTypeResponse?,
)