package eryaz.software.carParts.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eryaz.software.carParts.R
import eryaz.software.carParts.util.dialogs.ErrorDialog
import eryaz.software.carParts.util.dialogs.ProgressDialog
import eryaz.software.carParts.util.extensions.dpToPx

open class BaseBottomSheetDialogFragmentKt : BottomSheetDialogFragment() {
    var behavior: BottomSheetBehavior<View>? = null
    private var bottomSheet: FrameLayout? = null

    private val _progressDialog = ProgressDialog(lifecycle)
    private val _errorDialog = ErrorDialog(lifecycle)

    val progressDialog by lazy {
        return@lazy _progressDialog.createDialog(
            requireContext(),
            layoutInflater
        )
    }
    val errorDialog by lazy {
        return@lazy _errorDialog.createDialog(
            requireContext(),
            layoutInflater
        )
    }

    open val viewModel: BaseViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_Eryaz_BottomSheetDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.postDelayed({
            dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }, 300)

        init()
        setClicks()
        subscribeToObservables()
        fragmentResultListener()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState).apply {
            viewModel?.showErrorDialog?.asLiveData()
                ?.observe(this@BaseBottomSheetDialogFragmentKt) { errorDialog.show(context, it) }

            viewModel?.showProgressDialog?.asLiveData()
                ?.observe(this@BaseBottomSheetDialogFragmentKt) { progressDialog.setUiState(it) }

            setOnShowListener {
                val dialog = it as BottomSheetDialog

                val bottomSheet =
                    dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

                bottomSheet?.let {
                    bottomSheet.setBackgroundResource(android.R.color.transparent)

                    if (backgroundRounded()) {
                        view?.let { view ->
                            view.setBackgroundResource(R.drawable.bg_bottom_sheet_dialog)

                            view.setPadding(
                                view.paddingLeft,
                                32.dpToPx(),
                                view.paddingRight,
                                view.paddingBottom,
                            )
                        }
                    }
                    behavior = BottomSheetBehavior.from(bottomSheet)
                    behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior?.skipCollapsed = skipCollapsed()
                    behavior?.isHideable = hideable()

                    if (fullScreen()) {
                        it.updateLayoutParams<MarginLayoutParams> {
                            height = ViewGroup.LayoutParams.MATCH_PARENT
                        }
                    }

                    behavior?.removeBottomSheetCallback(bottomSheetCallback)
                    behavior?.addBottomSheetCallback(bottomSheetCallback)
                }

                onShowListener()
            }
        }

        return dialog
    }

    open val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (!hideable() && newState == BottomSheetBehavior.STATE_DRAGGING)
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()

        behavior?.removeBottomSheetCallback(bottomSheetCallback)
    }

    open fun backgroundRounded() = true

    open fun onBackPressed() {
        dismiss()
    }

    open fun hideable() = true
    open fun skipCollapsed() = true
    open fun fullScreen() = false

    open fun onShowListener() {
    }

    open fun init() {}
    open fun setClicks() {}
    open fun subscribeToObservables() {}
    open fun fragmentResultListener() {}
}
