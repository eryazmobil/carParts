package eryaz.software.carParts.data.models.dto

import androidx.annotation.StringRes
import eryaz.software.carParts.data.R


class ErrorDialogDto(
    var title: String? = "",
    var message: String? = "",
    @StringRes var titleRes: Int = 0,
    @StringRes var messageRes: Int = 0,
    var cancelable: Boolean = true,
    var showDialog: Boolean = true,
    var showNoInternetDialog: Boolean = true,
    var positiveButton: ButtonDto = ButtonDto(text = R.string.close),
    var negativeButton: ButtonDto = ButtonDto(),
    var dialogCanceled: (() -> Unit)? = null
    )