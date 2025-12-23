package eryaz.software.carParts.ui.dashboard.inbound.placement.placementDetail.crossdock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.carParts.databinding.CrossdockTransfersDialogLayoutBinding
import eryaz.software.carParts.ui.base.BaseDialogFragment
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.inbound.placement.PlacementListVM
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class CrossDockTransferDialogFragment : BaseFragment() {
    private val safeArgs by navArgs<CrossDockTransferDialogFragmentArgs>()

    override val viewModel by viewModel<CrossDockTransferVM> {
        parametersOf(safeArgs.productDto, safeArgs.crossDockDto, safeArgs.maxAmount)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        CrossdockTransfersDialogLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.executePendingBindings()

        return binding.root
    }

    override fun setClicks() {
        binding.shelfPlacement.setOnSingleClickListener {
           findNavController().navigateUp()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.placementToControlPoint.setOnSingleClickListener {
            viewModel.updateWaybillControlAddQuantity()
        }
    }

    override fun subscribeToObservables() {
        viewModel.navigateToBack
            .asLiveData()
            .observe(viewLifecycleOwner) {
                if (it) {
                    findNavController().navigateUp()
                }
            }
    }
}