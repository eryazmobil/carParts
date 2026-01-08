package eryaz.software.carParts.util.extensions

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.google.android.material.textfield.TextInputEditText
import kotlin.text.isNullOrEmpty
import kotlin.text.trim

fun TextInputEditText.onBarcodeOrShelfReadListener(
    func: () -> Unit
) {
    setOnEditorActionListener { _, actionId, event ->

        if (event?.action == KeyEvent.ACTION_UP) {
            false
        } else {
            val isValidBarcode = this.text?.trim().isNullOrEmpty().not()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                func()
            }
            hideSoftKeyboard()
            true
        }
    }
}