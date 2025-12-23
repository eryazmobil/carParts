package eryaz.software.carParts.util.extensions

import eryaz.software.carParts.data.persistence.SessionManager
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun String?.toIntOrZero(): Int {
    return this?.toIntOrNull() ?: 0
}

fun String?.toDoubleOrZero(): Double {
    return this?.toDoubleOrNull() ?: 0.0
}

fun String?.trimSpace(): String {
    return this?.replace(" ", "") ?: ""
}



fun String?.getFormattedDate(
    newFormat: String,
    currentFormat: String = "yyyy-MM-dd'T'HH:mm:ss"
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

fun String?.changeDate(
    dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss",
    newFormat: String,
    addYear: Int = 0,
    addMonth: Int = 0,
    addDay: Int = 0
): String? {
    val year = this.getFormattedDate(currentFormat = dateFormat, newFormat = "yyyy").toInt()
    val month = this.getFormattedDate(currentFormat = dateFormat, newFormat = "MM").toInt() - 1
    val day = this.getFormattedDate(currentFormat = dateFormat, newFormat = "d").toInt()

    val calendar = Calendar.getInstance()
    calendar[Calendar.YEAR] = year + addYear
    calendar[Calendar.MONTH] = month + addMonth
    calendar[Calendar.DAY_OF_MONTH] = day + addDay

    return calendarToDate(calendar, newFormat)
}