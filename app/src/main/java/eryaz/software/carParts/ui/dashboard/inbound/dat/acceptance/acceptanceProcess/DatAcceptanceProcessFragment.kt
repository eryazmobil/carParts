package eryaz.software.carParts.ui.dashboard.inbound.dat.acceptance.acceptanceProcess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.R
import eryaz.software.carParts.data.enums.IconType
import eryaz.software.carParts.data.models.dto.ButtonDto
import eryaz.software.carParts.data.models.dto.ConfirmationDialogDto
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.databinding.FragmentDatAcceptanceProcessDetailBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.dialogs.QuestionDialog
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.onBackPressedCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

class DatAcceptanceProcessFragment : BaseFragment() {

    override val viewModel by viewModel<DatAcceptanceProcessVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentDatAcceptanceProcessDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun subscribeToObservables() {
        viewModel.showProductDetailView.asLiveData().observe(viewLifecycleOwner) {
            binding.quantityEdt.requestFocus()
        }

        viewModel.controlSuccess.asLiveData().observe(viewLifecycleOwner) {
            if (viewModel.checkIfAllFinished()) {
                showFinishDialog()
            }
            binding.quantityEdt.setText("")
            binding.searchEdt.setText("")

            binding.searchEdt.requestFocus()
        }

        viewModel.showCreateBarcode.asLiveData().observe(viewLifecycleOwner) {
            QuestionDialog(
                onPositiveClickListener = {
                    findNavController().navigate(
                        DatAcceptanceProcessFragmentDirections.actionDatAcceptanceProcessFragmentToCreateBarcodeDialog()
                    )
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

        binding.searchEdt.setOnEditorActionListener { _, actionId, _ ->

            val isValidBarcode = viewModel.searchProduct.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getBarcodeByCode()
            }

            hideSoftKeyboard()
            true
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
                else -> viewModel.updateWaybillControlAddQuantity(viewModel.quantity.value.toInt())
            }
        }
    }

    private fun showFinishDialog() {
        showConditionDialog(
            ConfirmationDialogDto(
                title = getString(R.string.control_finished),
                message = getString(R.string.all_control_finished),
                positiveButton = ButtonDto(text = R.string.yes, onClickListener = {
                    backToPage()
                }),
                negativeButton = ButtonDto(
                    text = R.string.no,
                    onClickListener = { confirmationDialog.dismiss() })
            )
        )
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
                            DatAcceptanceProcessFragmentDirections.actionDatAcceptanceProcessFragmentToWaybillDetailListDialog()
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
        viewModel.actionIsFinished.asLiveData().observe(viewLifecycleOwner) {
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
