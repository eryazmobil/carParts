package eryaz.software.carParts.ui.dashboard.movement.transferStockCorrection.stockType

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.carParts.databinding.DialogStockTypeListBinding
import eryaz.software.carParts.ui.base.BaseDialogFragment
import eryaz.software.carParts.ui.dashboard.movement.transferStockCorrection.storageList.StorageListDialogFragmentArgs
import eryaz.software.carParts.util.adapter.inbound.adapter.StockTypeListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class StockTypeFragment : BaseDialogFragment() {
    private val safeArgs by navArgs<StorageListDialogFragmentArgs>()

    override val viewModel by viewModel<StockTyeDialogVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        DialogStockTypeListBinding.inflate(layoutInflater)
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
        adapter.onItemClick = {
            setFragmentResult(
                REQUEST_KEY, bundleOf(
                    ARG_STORAGE_DTO to it
                )
            )
            findNavController().navigateUp()
        }
    }

    override fun subscribeToObservables() {
        viewModel.stockTypeList.asLiveData()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        StockTypeListAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    companion object {
        const val REQUEST_KEY = "StockTypeFragment"
        const val ARG_STORAGE_DTO = "stock_type_dto"
    }
}