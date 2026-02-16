package eryaz.software.carParts.ui.dashboard.outbound.controlPoint.orderHeaderDialog.controlPointDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.carParts.R
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.databinding.FragmentControlPointDetailBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.outbound.controlPoint.orderHeaderDialog.controlPointDetail.addPackage.AddPackageControlDialog
import eryaz.software.carParts.ui.dashboard.recording.dialog.ProductListDialogFragment
import eryaz.software.carParts.ui.dashboard.settings.changeLanguage.LanguageFragment
import eryaz.software.carParts.util.adapter.outbound.ControlPointDetailListAdapter
import eryaz.software.carParts.util.adapter.outbound.ControlPointDetailListVH
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.observe
import eryaz.software.carParts.util.extensions.parcelable
import eryaz.software.carParts.util.extensions.toIntOrZero
import eryaz.software.carParts.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ControlPointDetailFragment : BaseFragment() {
    private val safeArgs by navArgs<ControlPointDetailFragmentArgs>()

    override val viewModel by viewModel<ControlPointDetailVM> {
        parametersOf(safeArgs.workActivityCode, safeArgs.orderHeaderId)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentControlPointDetailBinding.inflate(layoutInflater)
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
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.searchEdt.setOnEditorActionListener { _, actionId, _ ->
            val isValidBarcode = viewModel.searchProduct.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getBarcodeByCode()
            }

            hideSoftKeyboard()
            true
        }

        binding.searchProductTill.setEndIconOnClickListener {
            findNavController().navigate(ControlPointDetailFragmentDirections.actionControlPointDetailFragmentToProductListDialogFragment5())
        }

        binding.controlBtn.setOnSingleClickListener {
            viewModel.addQuantityForControl(viewModel.quantity.value.toIntOrZero())
        }

        binding.packageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        binding.packageSpinner.setSelection(position)
                        viewModel.setSelectedPackagePosition(position)
                    } else {
                        binding.packageSpinner.setSelection(
                            viewModel.packageList.value.indexOf(
                                viewModel.selectedPackageDto
                            )
                        )
                    }

                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {

                }
            }

        binding.detailPackage.setOnSingleClickListener {
            viewModel.getPackageList()
        }

        binding.addPackage.setOnSingleClickListener {
            findNavController().navigate(
                ControlPointDetailFragmentDirections.actionControlPointDetailFragmentToPackageListDialog(
                    viewModel.packageList.value.toTypedArray(), viewModel.orderHeaderId
                )
            )
        }

        binding.updatePackage.setOnSingleClickListener {
            if (viewModel.getSelectedPackagePosition() != 0) {
                findNavController().navigate(
                    ControlPointDetailFragmentDirections.actionControlPointDetailFragmentToUpdatePackageControlDialog(
                        viewModel.selectedPackageDto!!, viewModel.selectedPackageId
                    )
                )
            } else {
                toast(getString(R.string.pleaseSelectPackage))
            }
        }
    }

    override fun subscribeToObservables() {

        setFragmentResultListener(ProductListDialogFragment.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<ProductDto>(ProductListDialogFragment.ARG_PRODUCT_DTO)
            dto?.let {
                viewModel.setEnteredProduct(it)
            }
        }

        setFragmentResultListener(AddPackageControlDialog.REQUEST_KEY) { _, bundle ->
            bundle.getBoolean(AddPackageControlDialog.ARG_PACKAGE).let {
                viewModel.getPackageList()
            }
        }

        viewModel.successBarcode.asLiveData().observe(this) {
            if (it)
                binding.quantityEdt.requestFocus()
        }

        viewModel.searchList()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

        viewModel.serialCheckBox.observe(this) {
            if (binding.quantityEdt.hasFocus())
                binding.quantityEdt.hideSoftKeyboard()
        }

        viewModel.controlSuccess.asLiveData().observe(this) {
            if (it)
                binding.searchEdt.requestFocus()

        }

        viewModel.orderDetailList.observe(this) {
            adapter.submitList(it)
        }

        viewModel.packageList.observe(this) { list ->
            if (list.isNotEmpty()) {
                binding.stateView.setViewVisible(binding.packageSpinner, true)

            }
            context?.let {
                val adapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, list)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.packageSpinner.adapter = adapter
            }
        }

        viewModel.scrollToPosition
            .asLiveData()
            .observe(viewLifecycleOwner) {
                binding.recyclerView.scrollToPosition(it)

                (binding.recyclerView.findViewHolderForAdapterPosition(it) as? ControlPointDetailListVH)?.animateBackground()
            }
    }

    private val adapter by lazy {
        ControlPointDetailListAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

}
