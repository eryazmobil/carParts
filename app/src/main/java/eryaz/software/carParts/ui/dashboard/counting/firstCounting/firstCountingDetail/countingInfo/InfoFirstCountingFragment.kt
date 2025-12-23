package eryaz.software.carParts.ui.dashboard.counting.firstCounting.firstCountingDetail.countingInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.carParts.R
import eryaz.software.carParts.databinding.FragmentInfoFirstCountingBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class InfoFirstCountingFragment : BaseFragment() {
    private val safeArgs by navArgs<InfoFirstCountingFragmentArgs>()

    override val viewModel by viewModel<InfoFirstCountingVM> {
        parametersOf(safeArgs.stHeaderId, safeArgs.selectedShelfId)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentInfoFirstCountingBinding.inflate(layoutInflater)
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
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        productEdtAdapter.onEditClick = {
            if (it.editedShelfCurrentQuantity == it.shelfCurrentQuantity) {
                toast(getString(R.string.not_made))
            } else {
                viewModel.updateQuantitySTActionProcess(
                    stActionProcessId = it.id,
                    productId = it.productDto!!.id,
                    selectedShelfId = it.shelf!!.shelfId,
                    quantity = it.editedShelfCurrentQuantity!!.toInt()
                )
            }
        }

        productEdtAdapter.onDeleteClick = {
            viewModel.deleteSTActionProcess(stActionProcessId = it.id)
        }

    }

    override fun subscribeToObservables() {

        viewModel.stProcessList.asLiveData()
            .observe(viewLifecycleOwner) {
                productEdtAdapter.submitList(it)
            }

    }

    private val productEdtAdapter by lazy(LazyThreadSafetyMode.NONE) {
        eryaz.software.carParts.ui.dashboard.counting.adapter.InfoProductEdtAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    override fun hideActionBar() = false
}