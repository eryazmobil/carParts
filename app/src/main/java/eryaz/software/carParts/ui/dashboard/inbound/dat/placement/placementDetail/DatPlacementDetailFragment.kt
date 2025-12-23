package eryaz.software.carParts.ui.dashboard.inbound.dat.placement.placementDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.R
import eryaz.software.carParts.data.enums.IconType
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.databinding.FragmentDatPlacementDetailBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.adapter.inbound.adapter.SuggestionShelfListAdapter
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.dialogs.QuestionDialog
import eryaz.software.carParts.util.extensions.copyToClipboard
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.onBackPressedCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

class DatPlacementDetailFragment : BaseFragment() {
    override val viewModel by viewModel<DatPlacementDetailVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentDatPlacementDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.placementViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun setClicks() {

        onBackPressedCallback {
            QuestionDialog(
                onPositiveClickListener = {
                    backToPage()
                },
                textHeader = resources.getString(eryaz.software.carParts.data.R.string.exit),
                textMessage = resources.getString(eryaz.software.carParts.data.R.string.are_you_sure),
                positiveBtnText = resources.getString(eryaz.software.carParts.data.R.string.yes),
                negativeBtnText = resources.getString(R.string.no),
                singleBtnText = "",
                negativeBtnViewVisible = true,
                icType = IconType.Success.ordinal
            ).show(parentFragmentManager, "dialog")
        }

        binding.toolbar.setNavigationOnClickListener {
            backToPage()
        }

        binding.toolbar.setMenuOnClickListener {
            popupMenu(it)
        }

        binding.searchEdt.setOnEditorActionListener { _, actionId, _ ->

            val isValidBarcode = viewModel.searchProduct.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getBarcodeByCode()
            }

            hideSoftKeyboard()
            true
        }

        binding.shelfAddressEdt.setOnEditorActionListener { _, actionId, _ ->

            val isValidBarcode = viewModel.searchShelf.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getShelfByCode()
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
                            messageRes = R.string.msg_placement_qty_must_more_than_0
                        )
                    )
                }

                else -> QuestionDialog(
                    onPositiveClickListener = {
                        viewModel.updateWaybillControlAddQuantity()
                    },
                    textHeader = resources.getString(R.string.verify_qty),
                    textMessage = viewModel.resultAmountTxt.value,
                    positiveBtnText = resources.getString(eryaz.software.carParts.data.R.string.yes),
                    negativeBtnText = resources.getString(R.string.no),
                    negativeBtnViewVisible = true,
                    icType = IconType.Warning.ordinal
                ).show(parentFragmentManager, "dialog")

            }
        }

        adapter.onItemClick = {
            context?.copyToClipboard(it.shelfAddress)
            Toast.makeText(context, "Kopyalandı", Toast.LENGTH_SHORT).show()
        }
    }

    override fun subscribeToObservables() {
        viewModel.controlSuccess.asLiveData().observe(viewLifecycleOwner) {
            binding.searchEdt.setText("")
            binding.quantityEdt.setText("")
            binding.shelfAddressEdt.setText("")
            binding.searchEdt.requestFocus()
        }

        viewModel.productDetail.asLiveData().observe(viewLifecycleOwner) {
            binding.quantityEdt.requestFocus()
        }

        viewModel.suggestionShelfList.asLiveData().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.checkProductInPlacementList
            .asLiveData()
            .observe(viewLifecycleOwner) {
                if (it) {
                    //  showLottieAnimation(R.raw.placement_anim)
                    backToPage()
                }
            }
    }

    private val adapter by lazy {
        SuggestionShelfListAdapter().also {
            binding.suggestionShelfRecycleView.adapter = it
        }
    }

    private fun popupMenu(view: View) {
        PopupMenu(requireContext(), view).apply {
            inflate(R.menu.placement_menu)

            setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.menu_list_detail -> {
                        findNavController().navigate(
                            DatPlacementDetailFragmentDirections.actionDatPlacementDetailFragmentToWaybillDetailListDialog()
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