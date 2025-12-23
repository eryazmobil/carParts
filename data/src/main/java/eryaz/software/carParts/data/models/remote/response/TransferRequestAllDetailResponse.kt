package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class TransferRequestAllDetailResponse(
    @SerializedName("transferRequestHeader")
    val transferRequestHeader: TransferRequestHeaderResponse,
    @SerializedName("quantityShippedAll")
    val quantityShippedAll: Int,
    @SerializedName("quantityPickedAll")
    val quantityPickedAll: Int,
    @SerializedName("quantityAll")
    val quantityAll: Int,
    @SerializedName("transferRequestDetailPdaDto")
    val transferRequestDetailResponse: List<TransferRequestDetailResponse>
)