package eryaz.software.carParts.util.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioGroup
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton
import eryaz.software.carParts.data.enums.UiState

fun View.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
}

fun View.isVisibleElseInVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun RadioGroup.getCheckedPosition(): Int? {
    val checkedId = checkedRadioButtonId
    return if (checkedId > 0)
        indexOfChild(findViewById(checkedId))
    else
        null
}

fun TextSwitcher.setTextIfDifferent(text: String) {
    if ((this.currentView as TextView).text != text)
        setText(text)
}

fun ViewPager2.removeOverScroll() {
    (getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
}

fun ViewPager2.removeItemAnimator() {
    (getChildAt(0) as? RecyclerView)?.itemAnimator = null
}

fun View?.hideSoftKeyboard() {
    this?.let {
        val inputMethodManager =
            ContextCompat.getSystemService(it.context, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun DrawerLayout.lockMode(locked: Boolean) {
    val lockMode =
        if (locked) DrawerLayout.LOCK_MODE_LOCKED_CLOSED else DrawerLayout.LOCK_MODE_UNLOCKED
    this.setDrawerLockMode(lockMode)
}

fun MaterialButton.handleProgress(uiState: UiState, @ColorInt progressColor: Int = Color.BLACK) {
    isEnabled = uiState != UiState.LOADING

    if (!text.isNullOrEmpty() && tag == null)
        tag = text.toString()

    when (uiState) {
        UiState.LOADING ->
            showProgress { this.progressColor = progressColor }
        else ->
            hideProgress(newText = tag?.toString())
    }
}

fun View.setPaddingStart(px: Int) {
    setPadding(px, paddingTop, paddingEnd, paddingBottom)
}

fun View.setPaddingEnd(px: Int) {
    setPadding(paddingStart, paddingTop, px, paddingBottom)
}

fun View.setPaddingTop(px: Int) {
    setPadding(paddingStart, px, paddingEnd, paddingBottom)
}

fun View.setPaddingBottom(px: Int) {
    setPadding(paddingStart, paddingTop, paddingEnd, px)
}

fun View.setPaddingHorizontal(px: Int) {
    setPadding(px, paddingTop, px, paddingBottom)
}

fun View.setPaddingVertical(px: Int) {
    setPadding(paddingStart, px, paddingEnd, px)
}

fun TextView.setDrawableEnd(drawable: Drawable?) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
}

fun View.requestFocusOnPost() {
    post {
        requestFocus()
    }
}

fun View.addFocusChangeListener(colorResId: Int) {
    onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            setBackgroundColor(ContextCompat.getColor(context, colorResId))
        } else {
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }
    }
}

