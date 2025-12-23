package eryaz.software.carParts.util.widgets

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import eryaz.software.carParts.R

class TextInputLayout(context: Context, attrs: AttributeSet?) : TextInputLayout(context, attrs) {
    private var errorText: String? = ""

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed)
            addedEditText()
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TextInputLayout).apply {
            errorText = getString(R.styleable.TextInputLayout_errorText)

            recycle()
        }
    }

    private fun addedEditText() {
        editText?.run {
            setOnFocusChangeListener { _, _ -> checkFocus() }

            checkFocus()
        }
    }

    private fun checkFocus() {

    }
}