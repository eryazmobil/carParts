package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockTakingDetailDto(
    val id: Int,
    val stockTakingHeader: StockTakingHeaderDto?,
    val stockTakingType: WorkActivityTypeDto?
):Parcelable