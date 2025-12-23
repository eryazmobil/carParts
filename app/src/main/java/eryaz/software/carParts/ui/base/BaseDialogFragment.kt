package eryaz.software.carParts.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import eryaz.software.carParts.R
import eryaz.software.carParts.util.dialogs.ConfirmationDialog
import eryaz.software.carParts.util.dialogs.ErrorDialog
import eryaz.software.carParts.util.dialogs.ProgressDialog
import eryaz.software.carParts.util.dialogs.WarningDialog
import eryaz.software.carParts.util.extensions.observe

open class BaseDialogFragment : DialogFragment() {
    private val _progressDialog = ProgressDialog(lifecycle)
    private val _errorDialog = ErrorDialog(lifecycle)
    private val _warningDialog = WarningDialog(lifecycle)
    private val _confirmationDialog = ConfirmationDialog(lifecycle)

    val progressDialog by lazy(LazyThreadSafetyMode.NONE) {
        return@lazy _progressDialog.createDialog(
            requireContext(),
            layoutInflater
        )
    }
    val errorDialog by lazy(LazyThreadSafetyMode.NONE) {
        return@lazy _errorDialog.createDialog(
            requireContext(),
            layoutInflater
        )
    }

    val warningDialog by lazy(LazyThreadSafetyMode.NONE) {
        return@lazy _warningDialog.createDialog(
            requireContext(),
            layoutInflater
        )
    }
    val confirmationDialog by lazy(LazyThreadSafetyMode.NONE) {
        return@lazy _confirmationDialog.createDialog(
            requireContext(),
            layoutInflater
        )
    }

    open val viewModel: BaseViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClicks()
        subscribeToObservables()
    }

    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(activity as AppCompatActivity, R.style.Theme_Zeus_Dialog) {
            override fun onBackPressed() {
                this@BaseDialogFragment.onBackPressed()
            }
        }

        val window = dialog.window
        window?.attributes?.windowAnimations = getWindowAnimation()
        window?.decorView?.setBackgroundResource(R.color.transparent)
        viewModel?.showErrorDialog
            ?.observe(this) { errorDialog.show(context, it) }

        viewModel?.showConfirmationDialog
            ?.observe(this) { confirmationDialog.show(context, it) }

        viewModel?.showWarningDialog
            ?.observe(this) { warningDialog.show(context, it) }

        viewModel?.showProgressDialog
            ?.observe(this) { progressDialog.setUiState(it) }

        viewModel?.stringProvider = {
            context?.getString(it).orEmpty()
        }
        return dialog
    }

    open fun setClicks() {}
    open fun subscribeToObservables() {}

    open fun getWindowAnimation(): Int {
        return R.style.Theme_Zeus_Dialog
    }

    open fun onBackPressed() {
        dismiss()
    }
}