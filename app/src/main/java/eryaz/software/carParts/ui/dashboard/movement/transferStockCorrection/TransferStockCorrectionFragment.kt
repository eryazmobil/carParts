package eryaz.software.carParts.ui.dashboard.movement.transferStockCorrection

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
import eryaz.software.carParts.data.models.dto.StockTypeDto
import eryaz.software.carParts.data.models.dto.StorageDto
import eryaz.software.carParts.databinding.FragmentTransferStockCorrectionBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.movement.transferStockCorrection.stockType.StockTypeFragment
import eryaz.software.carParts.ui.dashboard.movement.transferStockCorrection.storageList.StorageListDialogFragment
import eryaz.software.carParts.ui.dashboard.recording.dialog.ProductListDialogFragment
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.parcelable
import eryaz.software.carParts.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferStockCorrectionFragment : BaseFragment() {
    override val viewModel by viewModel<TransferStockCorrectionVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentTransferStockCorrectionBinding.inflate(layoutInflater)
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

        binding.searchProductEdt.setOnEditorActionListener { _, actionId, _ ->
            val isValidBarcode = viewModel.searchProduct.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getBarcodeByCode()
            }

            hideSoftKeyboard()
            true
        }

        binding.searchProductBarcodeAcceptance.setEndIconOnClickListener {
            findNavController().navigate(
                TransferStockCorrectionFragmentDirections.actionTransferStockCorrectionFragmentToProductListDialogFragment()
            )
        }

        binding.shelfAddressEdt.setOnEditorActionListener { _, actionId, _ ->
            val isValidAddress = viewModel.enterShelfValue.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidAddress) {

                viewModel.getShelfByCode(viewModel.enterShelfValue.value.trim(), false)
            }

            hideSoftKeyboard()
            true
        }

        binding.choiceStorage.setOnSingleClickListener {
            findNavController().navigate(
                TransferStockCorrectionFragmentDirections.actionTransferStockCorrectionFragmentToStorageListDialogFragment(
                    false
                )
            )
        }

        binding.deleteProduct.setOnSingleClickListener {
            findNavController().navigate(
            TransferStockCorrectionFragmentDirections.actionTransferStockCorrectionFragmentToStockTypeFragment()
            )
        }

        binding.transferBtn.setOnSingleClickListener {
            viewModel.createStorageMovement()
        }
    }

    override fun subscribeToObservables() {

        setFragmentResultListener(StockTypeFragment.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<StockTypeDto>(StockTypeFragment.ARG_STORAGE_DTO)

            dto?.let {
                viewModel.setStockType(it)
            }
        }

        setFragmentResultListener(StorageListDialogFragment.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<StorageDto>(StorageListDialogFragment.ARG_STORAGE_DTO)

            dto?.let {
                binding.edtQuantity.requestFocus()
                viewModel.setStorage(it)
            }
        }

        setFragmentResultListener(ProductListDialogFragment.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<ProductDto>(ProductListDialogFragment.ARG_PRODUCT_DTO)
            dto?.let {
                viewModel.setEnteredProduct(it)
            }
        }

        viewModel.exitShelfId.asLiveData()
            .observe(viewLifecycleOwner) {
                if (it.toInt() != 0)
                    binding.edtQuantity.requestFocus()
            }

        viewModel.transferSuccess.asLiveData()
            .observe(viewLifecycleOwner) {
                if (it){
                    toast(getString(R.string.transfer_success))
                }
            }

    }

    override fun onStart() {
        super.onStart()

        binding.searchProductEdt.requestFocus()
    }

    override fun hideActionBar() = false
}