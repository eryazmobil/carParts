package eryaz.software.carParts.util

import android.content.Context


fun Context?.getScreenWidth() = this?.resources?.displayMetrics?.widthPixels ?: 0


fun Context?.getScreenHeight() = this?.resources?.displayMetrics?.heightPixels ?: 0


fun Context?.getStatusBarHeight(): Int {
    return this?.let {
        val idStatusBarHeight =
            it.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (idStatusBarHeight > 0) {
            it.resources.getDimensionPixelSize(idStatusBarHeight)
        } else 0
    } ?: 0
}

fun Context?.getNavigationBarHeight(): Int {
    return this?.let {
        val idStatusBarHeight =
            it.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (idStatusBarHeight > 0) {
            it.resources.getDimensionPixelSize(idStatusBarHeight)
        } else 0
    } ?: 0

}