package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WarehouseDto(
    val code: String,
    val name: String,
    val id: Int,
) : Parcelable