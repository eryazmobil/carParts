package eryaz.software.carParts.ui.dashboard.movement.transferStorage

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
import eryaz.software.carParts.data.models.dto.StorageDto
import eryaz.software.carParts.databinding.FragmentTransferStorageBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.movement.transferStockCorrection.storageList.StorageListDialogFragment
import eryaz.software.carParts.ui.dashboard.recording.dialog.ProductListDialogFragment
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.orZero
import eryaz.software.carParts.util.extensions.parcelable
import eryaz.software.carParts.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferStorageFragment : BaseFragment() {
    override val viewModel by viewModel<TransferStorageVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentTransferStorageBinding.inflate(layoutInflater)
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

        binding.exitShelfAddressEdt.setOnEditorActionListener { _, actionId, _ ->
            val isValidAddress = viewModel.exitShelfValue.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidAddress) {

                viewModel.getShelfByCode(viewModel.exitShelfValue.value.trim(), false)
            }

            hideSoftKeyboard()
            true
        }

        binding.enterShelfAddressEdt.setOnEditorActionListener { _, actionId, _ ->
            val isValidAddress = viewModel.enterShelfValue.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidAddress) {

                viewModel.getShelfByCode(viewModel.enterShelfValue.value.trim(), true)
            }

            hideSoftKeyboard()
            true
        }

        binding.choiceExitWarehouse.setOnSingleClickListener {
            findNavController().navigate(
                TransferStorageFragmentDirections.actionTransferStorageFragmentToStorageListDialogFragment(
                    false
                )
            )
        }

        binding.choiceEnterWarehouse.setOnSingleClickListener {
            findNavController().navigate(
                TransferStorageFragmentDirections.actionTransferStorageFragmentToStorageListDialogFragment(
                    true
                )
            )
        }

        binding.transferBtn.setOnSingleClickListener {
            viewModel.createStorageMovement()
        }

        binding.searchProductBarcode.setEndIconOnClickListener {
            findNavController().navigate(
                TransferStorageFragmentDirections.actionTransferStorageFragmentToProductListDialogFragment()
            )
        }

        binding.shelfAddressOut.setEndIconOnClickListener {
            findNavController().navigate(
                TransferStorageFragmentDirections.actionTransferStorageFragmentToShelfStorageDialog(
                    viewModel.productDetail.value?.id.orZero()
                )
            )
        }
    }

    override fun subscribeToObservables() {

        setFragmentResultListener(ProductListDialogFragment.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<ProductDto>(ProductListDialogFragment.ARG_PRODUCT_DTO)
            dto?.let {
                viewModel.setEnteredProduct(it)
            }
        }
        setFragmentResultListener(StorageListDialogFragment.REQUEST_KEY) { _, bundle ->
            val enterStorage = bundle.getBoolean(StorageListDialogFragment.ARG_ENTER_STORAGE)
            val dto = bundle.parcelable<StorageDto>(StorageListDialogFragment.ARG_STORAGE_DTO)

            if (enterStorage) {
                binding.enterShelfAddressEdt.requestFocus()
                dto?.let {
                    binding.enterShelfAddressEdt.requestFocus()
                    viewModel.setEnterStorage(it)
                }
            } else {
                binding.exitShelfAddressEdt.requestFocus()
                dto?.let {
                    binding.exitShelfAddressEdt.requestFocus()
                    viewModel.setExitStorage(it)
                }
            }
        }

        viewModel.exitShelfId.asLiveData()
            .observe(viewLifecycleOwner) {
                if (it.toInt() != 0)
                    binding.edtQuantity.requestFocus()
            }

        viewModel.transferSuccess.asLiveData()
            .observe(viewLifecycleOwner) {
                if (it) {
                    toast(getString(R.string.transfer_success))
                }
            }

    }

    override fun onStart() {
        super.onStart()

        binding.searchEdt.requestFocus()
    }

    override fun hideActionBar() = false
}