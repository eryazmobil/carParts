package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
class StockTypeDto(
    val type:Int,
    @StringRes var titleRes: Int = 0
): Parcelable