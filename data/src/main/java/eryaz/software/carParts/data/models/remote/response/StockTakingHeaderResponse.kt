package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class StockTakingHeaderResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("workActivity")
    val workActivity: WorkActivityResponse?,
    @SerializedName("stockTakingType")
    val stockTakingType: WorkActivityTypeResponse
)