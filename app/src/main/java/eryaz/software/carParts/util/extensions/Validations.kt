package eryaz.software.carParts.util.extensions

import android.text.TextUtils


fun String?.isValidPhoneNumber() = when {
    this?.startsWith("+994") == true -> this.trimSpace().length == 13
    this?.startsWith("0") == true -> this.trimSpace().length == 10
    else -> this.trimSpace().length == 9
}

fun String?.isValidOtp(): Boolean {
    return (this?.length ?: 0) == 4
}

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String?.isValidPassword(): Boolean {
    return (this?.length ?: 0) >= 4
}

fun String?.isValidUserId(): Boolean {
    return (this?.length ?: 0) >= 3
}

fun String?.isValidFinCode(): Boolean {
    return (this?.length ?: 0) >= 5
}

