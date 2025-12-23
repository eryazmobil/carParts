package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDto(
    val company: CompanyDto?,
    val code: String,
    val definition: String,
    val definition2: String,
    val manufacturer: String,
    val manufacturerCode: String,
    val unit: String,
    val hasSerial: Boolean,
    val id: Int
):Parcelable
