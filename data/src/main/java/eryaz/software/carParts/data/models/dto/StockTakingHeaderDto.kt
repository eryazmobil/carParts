package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockTakingHeaderDto(
    val id: Int,
    val workActivity: WorkActivityDto?,
    val stockTakingType: WorkActivityTypeDto?,
):Parcelable
