package eryaz.software.carParts.data.models.dto

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import eryaz.software.carParts.data.enums.DashboardPermissionType

data class DashboardItemDto(
    @DrawableRes
    val iconRes:Int,
    @StringRes
    val titleRes: Int,
    var type: DashboardPermissionType,
    var hasPermission: ObservableField<Boolean>
)