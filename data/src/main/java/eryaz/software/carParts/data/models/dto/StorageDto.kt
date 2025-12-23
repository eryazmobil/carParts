package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StorageDto(
    val id: Int,
    val warehouse: WarehouseDto,
    val code: String,
    val definition: String,
    val storageType: StorageTypeDto?,
) : Parcelable