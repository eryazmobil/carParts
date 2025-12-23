package eryaz.software.carParts.util.extensions

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.multidex.BuildConfig
import eryaz.software.carParts.data.R

fun Context?.getColorCompat(@ColorRes resId: Int) =
    this?.let {
        ContextCompat.getColor(this, resId)
    } ?: Color.BLACK

fun Context.openAppInGooglePlay() {
    val appId = BuildConfig.APPLICATION_ID
    try {
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appId")))
    } catch (_: ActivityNotFoundException) {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appId")
            )
        )
    }
}

fun Context.copyToClipboard(text: CharSequence){
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText("",text))
}

fun Context?.getColorInt(@ColorRes resId: Int): Int {
    this?.let {
        return ContextCompat.getColor(it, resId)
    }
    return Color.BLACK
}

fun Context?.getColorStateListByRes(@ColorRes resId: Int): ColorStateList {
    return ColorStateList.valueOf(this.getColorInt(resId))
}

fun Context?.getColorStateListByAttr(@AttrRes resId: Int): ColorStateList {
    return ColorStateList.valueOf(this.getColorAttrs(resId))
}

fun Context?.getColorAttrs(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    this?.theme?.resolveAttribute(resId, typedValue, true)

    return typedValue.data
}

fun Context?.getColorDrawable(@ColorRes resId: Int): Drawable {
    return ColorDrawable(getColorInt(resId))
}

fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable {
    return ContextCompat.getDrawable(this, resId)!!
}


fun Context?.isNetworkAvailable(): Boolean {
    this?.let {
        val connectivityManager =
            it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    return true
}

fun Context?.toast(message: Any?) {
    this?.let {
        Toast.makeText(it, message.toString(), Toast.LENGTH_SHORT).show()
    }
}

fun Context?.isNightModeApp(): Boolean {
    return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
}

fun Context?.isNightModeSystem(): Boolean {
    return getNightMode() == Configuration.UI_MODE_NIGHT_YES
}

fun Context?.getNightMode(): Int {
    return this?.let {
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    } ?: Configuration.UI_MODE_NIGHT_NO
}

fun Context?.share(text: String?) {
    this?.let {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        sendIntent.type = "text/plain"

        val chooserIntent = Intent.createChooser(sendIntent, R.string.share.toString())
        chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        it.startActivity(chooserIntent)
    }
}

fun Context?.getResourceId(
    variableName: String?,
    resourceName: String = "id"
): Int {
    return this?.let {
        try {
            resources.getIdentifier(variableName, resourceName, packageName)
        } catch (e: Exception) {
            -1
        }
    } ?: -1
}