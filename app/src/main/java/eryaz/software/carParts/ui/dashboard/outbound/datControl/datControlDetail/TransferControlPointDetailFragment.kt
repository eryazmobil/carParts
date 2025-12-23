package eryaz.software.carParts.ui.dashboard.outbound.datControl.datControlDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.carParts.databinding.FragmentTransferControlPointDetailBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.adapter.outbound.dat.TransferControlPointDetailListAdapter
import eryaz.software.carParts.util.adapter.outbound.dat.TransferControlPointDetailListVH
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TransferControlPointDetailFragment : BaseFragment() {
    private val safeArgs by navArgs<TransferControlPointDetailFragmentArgs>()

    override val viewModel by viewModel<TransferControlPointDetailVM> {
        parametersOf(safeArgs.id, safeArgs.workActivityCode)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentTransferControlPointDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        binding.controlBtn.setOnSingleClickListener {
            viewModel.addQuantityForControl(viewModel.enterQuantity.value.toInt())
        }
    }

    override fun subscribeToObservables() {
        viewModel.serialCheckBox.observe(this) {
            if (binding.quantityEdt.hasFocus()) binding.quantityEdt.hideSoftKeyboard()
        }

        viewModel.processSuccess.asLiveData()
            .observe(this) {
                if (it)
                    binding.searchEdt.requestFocus()
            }

        viewModel.showProductDetail.asLiveData()
            .observe(this) {
                if (it) {
                    binding.quantityEdt.requestFocus()
                }
            }

        viewModel.transferDetailList.asLiveData().observe(viewLifecycleOwner) {
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

        viewModel.scrollToPosition.asLiveData().observe(viewLifecycleOwner) {
            binding.recyclerView.scrollToPosition(it)

            (binding.recyclerView.findViewHolderForAdapterPosition(it) as? TransferControlPointDetailListVH)?.animateBackground()
        }
    }

    private val adapter by lazy {
        TransferControlPointDetailListAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    override fun onResume() {
        super.onResume()
        binding.searchEdt.requestFocus()
    }
}
