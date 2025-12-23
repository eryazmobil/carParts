package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CrossDockCheckDto(
    val id: Int,
    val quantity: String,
    val orderHeader: OrderHeaderDto
) : Parcelable
