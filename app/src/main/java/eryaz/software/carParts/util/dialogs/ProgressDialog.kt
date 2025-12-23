package eryaz.software.carParts.util.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import eryaz.software.carParts.R
import eryaz.software.carParts.data.enums.UiState
import eryaz.software.carParts.databinding.DialogProgressBinding

class ProgressDialog(lifecycle: Lifecycle) : LifecycleEventObserver {
    private lateinit var binding: DialogProgressBinding

    private var uiState: UiState = UiState.SUCCESS
    private var dialog: Dialog? = null

    init {
        lifecycle.addObserver(this)
    }

    fun createDialog(context: Context, layoutInflater: LayoutInflater): ProgressDialog {
        binding = DialogProgressBinding.inflate(layoutInflater)

        dialog = Dialog(context, R.style.customAlertDialog).apply {
            setContentView(binding.root)
            setCancelable(false)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                attributes?.windowAnimations = R.style.customAlertDialog
            }
        }
        return this
    }

    fun setUiState(uiState: UiState?) {
        uiState?.let {
            this.uiState = uiState

            show()
        }
    }

    private fun show() {
        if (uiState == UiState.LOADING)
            dialog?.show()
        else
            dismiss()
    }

    private fun dismiss() {
        dialog?.dismiss()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME ->
                show()
            Lifecycle.Event.ON_STOP ->
                dismiss()
            else -> {}
        }
    }
}