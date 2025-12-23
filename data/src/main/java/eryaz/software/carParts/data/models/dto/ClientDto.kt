package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class  ClientDto(
    val id: Int,
    val code: String,
    val name: String
):Parcelable