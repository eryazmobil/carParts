package eryaz.software.carParts.ui.dashboard.inbound.acceptance.acceptanceProcess

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.R
import eryaz.software.carParts.data.enums.IconType
import eryaz.software.carParts.data.models.dto.ButtonDto
import eryaz.software.carParts.data.models.dto.ConfirmationDialogDto
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.databinding.FragmentAcceptanceProcessDetailBinding
import eryaz.software.carParts.ui.EventBus
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.recording.dialog.ProductListDialogFragment
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.dialogs.QuestionDialog
import eryaz.software.carParts.util.extensions.handleProgress
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.onBackPressedCallback
import eryaz.software.carParts.util.extensions.onBarcodeOrShelfReadListener
import eryaz.software.carParts.util.extensions.parcelable
import eryaz.software.carParts.util.extensions.toIntOrZero
import org.koin.androidx.viewmodel.ext.android.viewModel

class AcceptanceProcessFragment : BaseFragment() {

    override val viewModel by viewModel<AcceptanceProcessVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAcceptanceProcessDetailBinding.inflate(layoutInflater)
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

    override fun subscribeToObservables() {

        setFragmentResultListener(ProductListDialogFragment.REQUEST_KEY) { _, bundle ->
            val dto = bundle.parcelable<ProductDto>(ProductListDialogFragment.ARG_PRODUCT_DTO)
            dto?.let {
                viewModel.setEnteredProduct(it)
            }
        }

        viewModel.showProductDetailView
            .asLiveData()
            .observe(viewLifecycleOwner) {
                binding.quantityEdt.requestFocus()
            }

        viewModel.uiState.asLiveData().observe(viewLifecycleOwner) { data ->
            binding.controlBtn.handleProgress(data, progressColor = Color.WHITE)
        }

        viewModel.controlSuccess
            .asLiveData()
            .observe(this) {
                if (it) {
                    showFinishDialog()
                }
                binding.quantityEdt.setText("")
                binding.searchEdt.setText("")

                binding.searchEdt.requestFocus()
            }

        viewModel.showCreateBarcode.observe(viewLifecycleOwner) { shouldShow ->
            if (shouldShow) {
                val alreadyOpen = parentFragmentManager.findFragmentByTag("dialog")

                if (alreadyOpen == null) {
                    QuestionDialog(
                        onPositiveClickListener = {
                            EventBus.navigateWithDeeplink.value =
                                getString(R.string.deeplinkCreateBarcode).toUri()
                        },
                        textHeader = resources.getString(eryaz.software.carParts.data.R.string.attention),
                        textMessage = resources.getString(eryaz.software.carParts.data.R.string.msg_no_barcode_and_new_barcode),
                        positiveBtnText = resources.getString(eryaz.software.carParts.data.R.string.yes),
                        negativeBtnText = resources.getString(eryaz.software.carParts.data.R.string.no),
                        negativeBtnViewVisible = true,
                        icType = IconType.Warning.ordinal
                    ).show(parentFragmentManager, "dialog")

                }
            }
        }
    }

    override fun setClicks() {
        onBackPressedCallback {
            showConditionDialog(
                ConfirmationDialogDto(
                    title = getString(R.string.exit),
                    message = getString(R.string.are_you_sure),
                    positiveButton = ButtonDto(text = R.string.yes, onClickListener = {
                        backToPage()
                    }),
                    negativeButton = ButtonDto(
                        text = R.string.no,
                        onClickListener = { confirmationDialog.dismiss() })
                )
            )
        }

        binding.toolbar.setMenuOnClickListener {
            popupMenu(it)
        }

        binding.toolbar.setNavigationOnClickListener {
            backToPage()
        }

        binding.quantityEdt.setOnEditorActionListener { _, actionId, _ ->
            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE)) {
                hideSoftKeyboard()
            }

            true
        }

        binding.searchProductBarcodeAcceptance.setEndIconOnClickListener {
            findNavController().navigate(
                AcceptanceProcessFragmentDirections.actionAcceptanceProcessFragmentToProductListDialogFragment()
            )
        }

        binding.searchEdt.onBarcodeOrShelfReadListener {
            viewModel.getBarcodeByCode()
        }

        binding.controlBtn.setOnSingleClickListener {
            when {
                viewModel.isQuantityValid() -> {
                    errorDialog.show(
                        context = context,
                        ErrorDialogDto(
                            messageRes = R.string.msg_control_qty_must_more_than_0
                        )
                    )
                }

                else -> viewModel.updateWaybillControlAddQuantity(viewModel.quantity.value.toIntOrZero())
            }
        }
    }

    private fun showFinishDialog() {
        showConditionDialog(
            ConfirmationDialogDto(
                title = getString(R.string.control_finished),
                message = getString(R.string.all_control_finished),
                positiveButton = ButtonDto(
                    text = R.string.yes,
                    onClickListener = {
                        backToPage()
                    }
                ),
                negativeButton = ButtonDto(
                    text = R.string.no,
                    onClickListener = {
                        findNavController().popBackStack()
                    }
                )
            ))
    }

    private fun popupMenu(view: View) {
        PopupMenu(requireContext(), view).apply {
            inflate(R.menu.acceptance_menu)

            val menuItem = menu.findItem(R.id.quick_control)
            menuItem.isChecked = viewModel.serialCheck.value

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.quick_control -> {
                        val newValue = !(viewModel.serialCheck.value)
                        viewModel.serialCheck.value = newValue
                        menuItem.isChecked = newValue
                        true
                    }

                    R.id.menu_list_detail -> {
                        findNavController().navigate(
                            AcceptanceProcessFragmentDirections
                                .actionAcceptanceProcessFragmentToWaybillDetailListDialog()
                        )
                        true
                    }

                    R.id.menu_finish_action -> {
                        backToPage()
                        true
                    }

                    else -> false
                }
            }

            show()
        }
    }

    private fun backToPage() {
        viewModel.finishWorkAction()
        viewModel.actionIsFinished
            .asLiveData()
            .observe(viewLifecycleOwner) {
                findNavController().navigateUp()
                TemporaryCashManager.getInstance().workActivity = null
                TemporaryCashManager.getInstance().workAction = null
            }
    }

    override fun onResume() {
        super.onResume()
        binding.searchEdt.requestFocus()
    }
}
