package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StorageTypeDto(
    val code: String,
    val definition: String,
    val id: Int
) : Parcelable