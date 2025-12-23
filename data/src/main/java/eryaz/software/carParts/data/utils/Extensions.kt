package eryaz.software.carParts.data.utils

import eryaz.software.carParts.data.persistence.SessionManager
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

fun String?.getFormattedDate(
    newFormat: String,
    currentFormat: String? = "yyyy-MM-dd'T'HH:mm:ss"
): String {
    this?.let { date ->
        try {
            val fmt = SimpleDateFormat(currentFormat, Locale(SessionManager.language.name.lowercase()))
            val myFmt = SimpleDateFormat(newFormat, Locale(SessionManager.language.name.lowercase()))
            fmt.parse(date)?.let {
                return myFmt.format(it)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    return this ?: ""
}
