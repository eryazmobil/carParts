package eryaz.software.carParts.ui.dashboard.recording.recordBarcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.R
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.databinding.FragmentRecordingBarcodeBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.recording.dialog.ProductListDialogFragment
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.observe
import eryaz.software.carParts.util.extensions.parcelable
import eryaz.software.carParts.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class BarcodeRecordingFragment : BaseFragment() {
    override val viewModel by viewModel<RecordBarcodeVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentRecordingBarcodeBinding.inflate(layoutInflater)
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

        binding.productCodeEdt.setOnEditorActionListener { _, actionId, _ ->
            val isValidBarcode = viewModel.searchProductCode.value.trim().isNotEmpty()

            if (((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE)) || actionId == EditorInfo.IME_ACTION_DONE && isValidBarcode) {
                viewModel.getProductByCode()
            }

            hideSoftKeyboard()
            true
        }

        binding.searchProductCodeLyt.setEndIconOnClickListener {
           findNavController().navigate(
               BarcodeRecordingFragmentDirections.actionBarcodeRecordingFragmentToProductListDialogFragment()
           )
        }

        binding.createBtn.setOnSingleClickListener {
            viewModel.createAddressMovement()
        }
    }

    override fun subscribeToObservables() {

        setFragmentResultListener(ProductListDialogFragment.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<ProductDto>(ProductListDialogFragment.ARG_PRODUCT_DTO)
            dto?.let {
                viewModel.setEnteredProduct(it)
            }
        }

        viewModel.showProductDetail.observe(this) {
            if (it) {
                binding.productBarcodeEdt.requestFocus()
            }
        }

        viewModel.createdBarcode.asLiveData()
            .observe(viewLifecycleOwner) {
                toast(getString(R.string.msg_process_success))
                binding.productCodeEdt.requestFocus()
            }

        viewModel.showProductDetail.observe(this) {
            if(it){
                binding.productBarcodeEdt.requestFocus()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.productCodeEdt.requestFocus()
    }

    override fun hideActionBar() = false
}