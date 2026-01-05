package eryaz.software.carParts.ui.dashboard.outbound.orderPicking.orderPickingDetail

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
import eryaz.software.carParts.data.models.dto.ButtonDto
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.databinding.FragmentOrderPickingDetailBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.recording.dialog.ProductListDialogFragment
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.observe
import eryaz.software.carParts.util.extensions.onBackPressedCallback
import eryaz.software.carParts.util.extensions.parcelable
import eryaz.software.carParts.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderPickingDetailFragment : BaseFragment() {
    override val viewModel by viewModel<OrderPickingDetailVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentOrderPickingDetailBinding.inflate(layoutInflater)
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
        onBackPressedCallback {
            viewModel.checkCrossDockNeedByActionId()
        }

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.toolbar.setMenuOnClickListener {
            popupMenu(it)
        }

        binding.searchProductBarcodeTill.setEndIconOnClickListener {
            findNavController().navigate(
                OrderPickingDetailFragmentDirections.actionOrderPickingDetailFragmentToProductListDialogFragment()
            )
        }

        binding.shelfListBtn.setOnSingleClickListener {
            findNavController().navigate(
                OrderPickingDetailFragmentDirections.actionOrderPickingDetailFragmentToShelfListDialog(
                    viewModel.productId
                )
            )
        }

        binding.pickProductBtn.setOnSingleClickListener {
            viewModel.updateOrderDetailCollectedAddQuantityForPda()
        }
    }

    override fun subscribeToObservables() {

        setFragmentResultListener(ProductListDialogFragment.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<ProductDto>(ProductListDialogFragment.ARG_PRODUCT_DTO)
            dto?.let {
                viewModel.setEnteredProduct(it)
            }
        }

        viewModel.productRequestFocus.asLiveData().observe(this) {
            if (it)
                binding.searchProductEdt.requestFocus()
        }

        viewModel.parentView.observe(this) {
            if (it) {
                binding.parentView.visibility = View.GONE

                errorDialog.show(
                    context, ErrorDialogDto(
                        titleRes = R.string.warning,
                        messageRes = R.string.order_was_picking,
                        positiveButton = ButtonDto(text = R.string.close_screen, onClickListener = {
                            viewModel.checkCrossDockNeedByActionId()
                        })
                    )
                )
            }
        }

        viewModel.stockNotEnough.asLiveData().observe(this) {
            if (it) {
                binding.parentView.visibility = View.GONE
                errorDialog.show(
                    context, ErrorDialogDto(
                        titleRes = R.string.warning,
                        messageRes = R.string.work_activity_error_1,
                        positiveButton = ButtonDto(text = R.string.close_screen, onClickListener = {
                            viewModel.checkCrossDockNeedByActionId()
                        })
                    )
                )
            }
        }

        viewModel.createStockOut.observe(this) {
            if (it) {
                toast(getString(R.string.success))
            }
        }

        viewModel.showProductDetail.observe(this) {
            if (it) {
                binding.shelfAddressEdt.requestFocus()
                binding.stateView.setViewVisible(binding.productDetailParent, true)
            } else {
                binding.searchProductEdt.requestFocus()
            }
        }

        binding.searchProductEdt.setOnEditorActionListener { _, actionId, _ ->

            val isValidBarcode = viewModel.productBarcode.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getBarcodeByCode()
            }

            hideSoftKeyboard()
            true
        }

        binding.shelfAddressEdt.setOnEditorActionListener { _, actionId, _ ->

            val shelfAddress = viewModel.shelfAddress.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && shelfAddress) {
                viewModel.getShelfByCode()
            }

            hideSoftKeyboard()
            true
        }

        viewModel.nextOrder.observe(this) {
            if (it) {
                binding.nextBtn.performClick()
            }
        }

        viewModel.shelfRead.asLiveData().observe(viewLifecycleOwner) {
            if (it) {
                binding.quantityEdt.requestFocus()
            }
        }

        viewModel.finishWorkAction.observe(this) {
            if (it) {
                findNavController().navigateUp()
            }
        }
    }

    private fun popupMenu(view: View) {
        PopupMenu(requireContext(), view).apply {
            inflate(R.menu.order_picking_menu)

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.order_detail_list -> {
                        findNavController().navigate(
                            OrderPickingDetailFragmentDirections.actionOrderPickingDetailFragmentToOrderDetailListDialog()
                        )
                        true
                    }

                    R.id.menu_finish_action -> {
                        activity?.onBackPressed()
                        true
                    }

                    R.id.refresh_order -> {
                        viewModel.getOrderDetailPickingList()
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
        binding.searchProductEdt.requestFocus()
    }
}