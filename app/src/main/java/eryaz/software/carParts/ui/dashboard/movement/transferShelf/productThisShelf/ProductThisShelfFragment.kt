package eryaz.software.carParts.ui.dashboard.movement.transferShelf.productThisShelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import eryaz.software.carParts.databinding.FragmentProductThisShelfBinding
import eryaz.software.carParts.ui.base.BaseDialogFragment
import eryaz.software.carParts.ui.dashboard.query.adapter.ShelfProductQuantityAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductThisShelfFragment : BaseDialogFragment() {
    private val safeArgs by navArgs<ProductThisShelfFragmentArgs>()

    override val viewModel by viewModel<ProductThisShelfVM> {
        parametersOf(safeArgs.productId)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentProductThisShelfBinding.inflate(layoutInflater)
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

        viewModel.shelfList.asLiveData()
            .observe(viewLifecycleOwner) {
                shelfAdapter.submitList(it)
            }
    }

    private val shelfAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ShelfProductQuantityAdapter().also {
            binding.recyclerView.adapter = it
        }
    }
}