package eryaz.software.carParts.util.extensions

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import eryaz.software.carParts.ui.base.BaseActivity
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

fun AppCompatActivity.findNavHostNavController(id: Int): NavController {
    val navHost = supportFragmentManager.findFragmentById(id) as NavHost
    return navHost.navController
}

fun BaseActivity.changeWindowBackground(drawable: Drawable?) {
    getContentView().background = drawable
}

//fun BaseActivity.changeActionBarBackground(drawable: Drawable?) {
//    getToolbar().background = drawable
//}

//fun BaseActivity.changeToolbarTextColor(isLightText: Boolean) {
//    val color =
//        if (isLightText) getColorInt(R.color.white) else getColorInt(R.color.black)
//
//    getToolbar().setTitleTextColor(color)
//    getToolbar().navigationIcon = getDrawableCompat(R.drawable.ic_arrow_back_24)
//    getToolbar().setNavigationIconTint(color)
//
//}

fun Activity.hideSoftKeyboard() {
    if (KeyboardVisibilityEvent.isKeyboardVisible(this))
        window.decorView.hideSoftKeyboard()
}


