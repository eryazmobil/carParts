package eryaz.software.carParts.ui.dashboard.movement.transferShelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.R
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.databinding.FragmentTransferAddressBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.recording.dialog.ProductListDialogFragment
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.observe
import eryaz.software.carParts.util.extensions.orZero
import eryaz.software.carParts.util.extensions.parcelable
import eryaz.software.carParts.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferShelfFragment : BaseFragment() {
    override val viewModel by viewModel<TransferShelfVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentTransferAddressBinding.inflate(layoutInflater)
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

        binding.transferBtn.setOnSingleClickListener {
            viewModel.createAddressMovement()
        }

        binding.searchProductBarcode.setEndIconOnClickListener {
            findNavController().navigate(
                TransferShelfFragmentDirections.actionTransferAddressFragmentToProductListDialogFragment()
            )
        }

        binding.shelfAddressOut.setEndIconOnClickListener {

            findNavController().navigate(
                TransferShelfFragmentDirections.actionTransferAddressFragmentToShelfStorageDialog(
                    viewModel.productDetail.value?.id.orZero()
                )
            )

        }

        binding.toolbar.setMenuOnClickListener {
            popupMenu(it)
        }
    }

    override fun subscribeToObservables() {

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
                toast(getString(R.string.transfer_success))
                binding.searchEdt.requestFocus()
            }

        viewModel.showProductDetail.observe(this) {
            if (it) {
                binding.exitShelfAddressEdt.requestFocus()
            }
        }

    }

    private fun popupMenu(view: View) {
        PopupMenu(requireContext(), view).apply {
            inflate(R.menu.movement_shelf)

            setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.movement_shelf -> {
                        findNavController().navigate(
                            TransferShelfFragmentDirections.actionTransferAddressFragmentToProductThisShelfFragment(
                                viewModel.productDetail.value?.id.orZero()
                            )
                        )
                        true
                    }

                    else -> false
                }
            }
            show()
        }
    }

    override fun onStart() {
        super.onStart()

        binding.searchEdt.requestFocus()
    }

    override fun hideActionBar() = false
}