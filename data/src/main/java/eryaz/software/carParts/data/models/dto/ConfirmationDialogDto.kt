package eryaz.software.carParts.data.models.dto

import androidx.annotation.StringRes

class ConfirmationDialogDto(
    var title: String? = "",
    var message: String? = "",
    @StringRes var titleRes: Int = 0,
    @StringRes var messageRes: Int = 0,
    var cancelable: Boolean = true,
    var showDialog: Boolean = true,
    var positiveButton: ButtonDto = ButtonDto(),
    var negativeButton: ButtonDto = ButtonDto(text = 0),
    var dialogCanceled: (() -> Unit)? = null,
    var showNoInternetDialog: Boolean = true

)
