package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShelfDto(
    val shelfId: Int,
    val quantity: String,
    val shelfAddress:String
):Parcelable
