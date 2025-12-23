package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetailDto(
    val id: Int,
    val orderHeader: OrderHeaderDto?,
    val product: ProductDto,
    val quantity: String,
    val quantityShipped: String,
    val quantityCollected: String
) : Parcelable