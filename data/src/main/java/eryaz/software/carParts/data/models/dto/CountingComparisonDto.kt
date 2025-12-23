package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import androidx.databinding.ObservableField
import kotlinx.parcelize.Parcelize

@Parcelize
class CountingComparisonDto(
    val productDto:ProductDto,
    var oldQuantity:String,
    var newQuantity: ObservableField<String>
):Parcelable
