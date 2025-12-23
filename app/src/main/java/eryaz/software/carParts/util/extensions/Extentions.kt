package eryaz.software.carParts.util.extensions
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun Drawable?.setColorFilter(@ColorInt color: Int) {
    this?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}

fun List<*>.toArrayList(): ArrayList<*> {
    return ArrayList(this)
}

suspend fun <T> MutableStateFlow<List<T>>.addItem(index: Int? = null, item: T) {
    val list = value.toMutableList()
    list.add(index ?: list.size, item)

    emit(list)
}

suspend fun <T> MutableStateFlow<List<T>>.removeItem(item: T) {
    val list = value.toMutableList()

    if (list.contains(item)) {
        list.remove(item)

        emit(list.toList())
    }
}

suspend fun <T> MutableStateFlow<List<T>>.replaceItem(oldItem: T, newItem: T) {
    val list = value.toMutableList()
    val index = list.indexOf(oldItem)

    index.let {
        if (index >= 0) {

            list.removeAt(it)
            list.add(it, newItem)

            emit(list.toList())
        }
    }
}

fun <T> ObservableField<T>.onChanged(value: (T) -> Unit) {
    addOnPropertyChangedCallback(object :
        Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            value.invoke(get()!!)
        }
    })

    value.invoke(get()!!)
}

fun Drawable?.setTintDrawable(@ColorInt color: Int): Drawable? {
    return this?.let {
        var drawable = this

        drawable = DrawableCompat.wrap(drawable)

        DrawableCompat.setTint(drawable, color)

        drawable
    }
}

fun Calendar.getFormattedDate(format: String): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(time)
}

fun <T> StateFlow<T>.observe(
    fragment: Fragment,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    result: (T) -> Unit
) {
    fragment.run {
        lifecycleScope.launch {
            repeatOnLifecycle(state) {
                collect { result(it) }
            }
        }
    }
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

fun <T> T?.ifNull(value: () -> T) = this ?: value()

fun String?.ifNullOrEmpty(value: () -> String) = if (this.isNullOrEmpty()) value() else this