package eryaz.software.carParts.ui.dashboard.outbound.controlPoint.orderHeaderDialog.controlPointDetail.addPackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.carParts.R
import eryaz.software.carParts.databinding.DialogAddPackageControlBinding
import eryaz.software.carParts.ui.base.BaseDialogFragment
import eryaz.software.carParts.ui.dashboard.recording.dialog.ProductListDialogFragment
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.observe
import eryaz.software.carParts.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddPackageControlDialog : BaseDialogFragment() {
    private val safeArgs by navArgs<AddPackageControlDialogArgs>()

    override val viewModel by viewModel<AddPackageControlVM> {
        parametersOf(safeArgs.packgeList.toList(), safeArgs.orderHeaderId)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        DialogAddPackageControlBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun setClicks() {
        binding.saveBtn.setOnSingleClickListener {
            viewModel.createPackage()
        }
    }

    override fun subscribeToObservables() {
        viewModel.savePackage.observe(this) {
            if (it) {
                toast(getString(R.string.packageAdded))
                setFragmentResult(
                    REQUEST_KEY, bundleOf(
                        ARG_PACKAGE to true,
                    )
                )

                findNavController().navigateUp()
            }
        }
    }

    companion object {
        const val REQUEST_KEY = "AddPackageControlDialog"
        const val ARG_PACKAGE = "package"
    }
}