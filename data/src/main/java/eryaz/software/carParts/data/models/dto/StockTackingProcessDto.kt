package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockTackingProcessDto(
    val id: Int,
    val stockTakingDetail: StockTakingDetailDto?,
    val shelf: ShelfDto?,
    val shelfCurrentQuantity: Double?,
    var editedShelfCurrentQuantity: Double?,
    val productDto: ProductDto?
):Parcelable