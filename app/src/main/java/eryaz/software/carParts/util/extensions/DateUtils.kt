package eryaz.software.carParts.util.extensions

import android.annotation.SuppressLint
import eryaz.software.carParts.data.persistence.SessionManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
fun getCalendar(
    date: String?,
    format: String? = "yyyy-MM-dd'T'HH:mm:ss",
): Calendar {
    try {
        SimpleDateFormat(format).parse(date ?: "")?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.time

            return calendar
        }
    } catch (e: Exception) {
    }
    return Calendar.getInstance()
}

@SuppressLint("SimpleDateFormat")
fun calendarToDate(calendar: Calendar, newPattern: String?): String? {
    var date: String? =
        calendar[Calendar.YEAR].toString() + "-" +
                (calendar[Calendar.MONTH] + 1) + "-" +
                calendar[Calendar.DAY_OF_MONTH] + " " +
                calendar[Calendar.HOUR_OF_DAY] + ":" +
                calendar[Calendar.MINUTE]

    date = try {
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val myFmt = SimpleDateFormat(newPattern, Locale(SessionManager.language.name.lowercase()))

        fmt.parse(date ?: "")?.let {
            return myFmt.format(it)
        }
    } catch (e: ParseException) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Calendar.getInstance().time)
    }
    return date
}