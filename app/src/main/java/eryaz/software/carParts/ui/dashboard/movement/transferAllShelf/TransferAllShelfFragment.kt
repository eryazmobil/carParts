package eryaz.software.carParts.ui.dashboard.movement.transferAllShelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.R
import eryaz.software.carParts.databinding.FragmentTransferAllShelfBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import eryaz.software.carParts.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferAllShelfFragment : BaseFragment() {
    override val viewModel by viewModel<TransferAllShelfVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentTransferAllShelfBinding.inflate(layoutInflater)
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
    }

    override fun subscribeToObservables() {
        viewModel.exitShelfId.asLiveData()
            .observe(viewLifecycleOwner) {
                if (it.toInt() != 0)
                    binding.enterShelfAddressEdt.requestFocus()
            }

        viewModel.transferSuccess.asLiveData()
            .observe(viewLifecycleOwner) {
                toast(getString(R.string.transfer_success))
                binding.exitShelfAddressEdt.requestFocus()
            }
    }

    override fun onStart() {
        super.onStart()

        binding.exitShelfAddressEdt.requestFocus()
    }

    override fun hideActionBar() = false
}