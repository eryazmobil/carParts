package eryaz.software.carParts.ui.dashboard.counting.firstCounting.firstCountingDetail.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.asLiveData
import eryaz.software.carParts.R
import eryaz.software.carParts.data.enums.IconType
import eryaz.software.carParts.databinding.CreateBarcodeDialogBinding
import eryaz.software.carParts.ui.base.BaseDialogFragment
import eryaz.software.carParts.ui.dashboard.inbound.acceptance.acceptanceProcess.createBarcode.CreateBarcodeDialogVM
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.dialogs.QuestionDialog
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.showLottieAnimation
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateBarcodeDialog : BaseDialogFragment() {
    override val viewModel by viewModel<CreateBarcodeDialogVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        CreateBarcodeDialogBinding.inflate(layoutInflater)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputProductCode.requestFocus()

        binding.inputProductCode.setOnEditorActionListener { _, actionId, _ ->
            val isValidBarcode = viewModel.productCode.value.trim().isNotEmpty()
            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) && isValidBarcode) {
                viewModel.getProductByCode()
            }

            hideSoftKeyboard()
            true
        }

        subscribe()

        binding.saveBtn.setOnSingleClickListener {
            viewModel.createBarcode()
        }

        viewModel.showErrorProductMessage
            .asLiveData()
            .observe(viewLifecycleOwner) {
                QuestionDialog(
                    textHeader = resources.getString(eryaz.software.carParts.data.R.string.attention),
                    textMessage = it,
                    singleBtnText = resources.getString(eryaz.software.carParts.data.R.string.okay),
                    negativeBtnViewVisible = false,
                    icType = IconType.Warning.ordinal
                ).show(parentFragmentManager, "dialog")
            }

        viewModel.showSuccess
            .asLiveData()
            .observe(viewLifecycleOwner) {
                showLottieAnimation(R.raw.lottie_product_updated)
                dismiss()
            }

    }

    private fun subscribe() {
        viewModel.showErrorProduct
            .asLiveData()
            .observe(viewLifecycleOwner) {
                QuestionDialog(
                    textHeader = resources.getString(eryaz.software.carParts.data.R.string.attention),
                    textMessage = resources.getString(eryaz.software.carParts.data.R.string.msg_no_no_product),
                    singleBtnText = resources.getString(eryaz.software.carParts.data.R.string.okay),
                    negativeBtnViewVisible = false,
                    icType = IconType.Warning.ordinal
                ).show(parentFragmentManager, "dialog")

            }

        viewModel.showProductViewDetail
            .asLiveData()
            .observe(viewLifecycleOwner) {
                binding.inputProductBarcode.requestFocus()
            }
    }

}
