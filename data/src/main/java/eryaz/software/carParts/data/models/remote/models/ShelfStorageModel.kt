package eryaz.software.carParts.data.models.remote.models

import android.os.Parcelable
import eryaz.software.carParts.data.models.dto.ShelfDto
import eryaz.software.carParts.data.models.dto.StorageDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShelfStorageModel(
    val shelfDto: ShelfDto?,
    val storageDto: StorageDto?,
    val quantity: String?
) : Parcelable
