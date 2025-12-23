package eryaz.software.carParts.util.bindingAdapter

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import eryaz.software.carParts.util.OnSingleClickListener
import eryaz.software.carParts.util.extensions.dpToPx
import eryaz.software.carParts.util.extensions.toDoubleOrZero
import eryaz.software.carParts.util.getStatusBarHeight
import eryaz.software.carParts.util.widgets.DividerItemDecoration
import eryaz.software.carParts.util.widgets.TextInputLayout

@BindingAdapter("itemDecoration")
fun RecyclerView.setItemDecoration(spaceDp: Int) {
    addItemDecoration(DividerItemDecoration(spaceDp.dpToPx()))
}

@BindingAdapter("showFab")
fun FloatingActionButton.showFab(show: Boolean?) {
    if (show == true)
        show()
    else
        hide()
}

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

@BindingAdapter("textRes")
fun TextView.setText(resId: Int) {
    if (resId > 0)
        this.setText(resId)
}

@BindingAdapter("selected")
fun View.isSelected(selected: Boolean?) {
    this.isSelected = selected == true
}

@BindingAdapter("fitsSystemWindows")
fun View.fitsSystemWindows(boolean: Boolean?) {
    setPadding(
        this.paddingLeft,
        context.getStatusBarHeight(),
        this.paddingRight,
        this.paddingBottom
    )
}

@BindingAdapter("nextFocusView", "nextFocusWhen", requireAll = true)
fun TextInputLayout.nextFocusView(nextEdt: TextInputLayout?, next: Boolean?) {
    if (next == true)
        nextEdt?.requestFocus()
}

@BindingAdapter("showSoftKeyboard")
fun EditText.showSoftKeyboard(boolean: Boolean?) {
    context?.let {
        postDelayed({
            requestFocus()
            setSelection(text.length)

            val imm = ContextCompat.getSystemService(context, InputMethodManager::class.java)
            imm?.showSoftInput(this, 0)
        }, 10)
    }
}


@BindingAdapter("isVisible")
fun View.setVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("isVisibleElseGone")
fun View.setVisibleElseGone(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("enabled")
fun View.isEnabled(enabled: Boolean?) {
    this.isEnabled = enabled == true
}

@BindingAdapter("smoothProgress")
fun LinearProgressIndicator.setSmoothProgress(progress: Int) {
    val animation = ObjectAnimator.ofInt(this, "progress", progress)
    animation.duration = 300
    animation.interpolator = LinearInterpolator()
    animation.start()
}

@BindingAdapter("isChecked")
fun CheckBox.setChecked(isChecked: Boolean) {
    this.isChecked = isChecked
}

@BindingAdapter("paymentMaxAmount", requireAll = true)
fun EditText.paymentAmount(maxAmount: Double) {
    doAfterTextChanged {
        text.toString().run {
            val currentAmount = toDoubleOrZero()

            // 101 > 100
            if (currentAmount > maxAmount) {
                if (maxAmount.toInt() < maxAmount) {    // 100.1 > 100 -> 100.1
                    setText(maxAmount.toString())
                } else                                  // 100.0 == 100 -> 100
                    setText(maxAmount.toInt().toString())

                setSelection(length())
            }

            // .0 -> 0.
            if (startsWith(".")) {
                setText(replace(".", "0."))
                setSelection(length())
            }

            // 100.000 -> 100.00
            if (lastIndexOf(".") > 0 && lastIndexOf(".") < length() - 3) {
                setText(substring(0, length() - 1))
                setSelection(length())
            }

            // 100.000 -> 100.00
            if (length() >= 2 && startsWith("0") && substring(0, 2).contains(".").not()) {
                setText(substring(1, length()))
                setSelection(length())
            }
        }
    }
}