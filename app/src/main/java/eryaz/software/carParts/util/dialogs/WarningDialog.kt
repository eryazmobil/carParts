package eryaz.software.carParts.util.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import eryaz.software.carParts.R
import eryaz.software.carParts.data.models.dto.WarningDialogDto
import eryaz.software.carParts.databinding.DialogWarningMessageBinding
import eryaz.software.carParts.util.extensions.isNetworkAvailable

class WarningDialog(lifecycle: Lifecycle) : LifecycleEventObserver {
    private lateinit var binding: DialogWarningMessageBinding
    private var model: WarningDialogDto? = null
    private var dialog: Dialog? = null

    init {
        lifecycle.addObserver(this)
    }

    fun createDialog(context: Context, layoutInflater: LayoutInflater): WarningDialog {
        binding = DialogWarningMessageBinding.inflate(layoutInflater)

        dialog = Dialog(context, R.style.AlertDialogTheme).apply {
            setContentView(binding.root)
            setOnCancelListener {
                model?.showDialog = false
            }
            setOnDismissListener {
                model?.showDialog = false
            }

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                attributes?.windowAnimations = R.style.AlertDialogTheme
            }
        }
        return this
    }

    fun show(context: Context?, model: WarningDialogDto?) {
        model?.let {
            if (!context.isNetworkAvailable()) {
                model.message = context?.getString(R.string.in_error_dialog_no_internet_connection)
            }

            if (model.titleRes > 0)
                model.title = context?.getString(model.titleRes)

            if (model.messageRes > 0)
                model.message = context?.getString(model.messageRes)

            if (model.title.isNullOrEmpty() && model.message.isNullOrEmpty())
                return

            this.model = model
            binding.model = model

            if (model.completeButton.onClickListener == null)
                model.completeButton.onClickListener = View.OnClickListener { dismiss() }

            dialog?.setCancelable(model.cancelable)

            show()
        }
    }

    private fun show() {
        if (model?.showDialog == true)
            dialog?.show()
    }

    fun dismiss() {
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