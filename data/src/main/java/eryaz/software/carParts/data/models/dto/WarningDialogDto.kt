package eryaz.software.carParts.data.models.dto

import androidx.annotation.StringRes


class WarningDialogDto(
    @StringRes var titleRes: Int = 0,
    @StringRes var messageRes: Int = 0,
    var title: String? = "",
    var message: String? = "",
    var cancelable: Boolean = true,
    var showDialog: Boolean = true,
    var completeButton: ButtonDto = ButtonDto(),
)
