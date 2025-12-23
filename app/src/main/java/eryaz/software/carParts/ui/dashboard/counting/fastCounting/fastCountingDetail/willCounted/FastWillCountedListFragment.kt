package eryaz.software.carParts.ui.dashboard.counting.fastCounting.fastCountingDetail.willCounted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import eryaz.software.carParts.databinding.FragmentFastWillCountedListBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.counting.fastCounting.adapter.WillCountedProductListAdapter
import eryaz.software.carParts.ui.dashboard.counting.fastCounting.dialog.EditedQuantityDialog
import eryaz.software.carParts.util.extensions.observe
import eryaz.software.carParts.util.extensions.orZero
import eryaz.software.carParts.util.extensions.toIntOrZero
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FastWillCountedListFragment : BaseFragment() {
    private val safeArgs by navArgs<FastWillCountedListFragmentArgs>()

    override val viewModel by viewModel<FastWillCountedListVM> {
        parametersOf(safeArgs.productList.toList())
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentFastWillCountedListBinding.inflate(layoutInflater)
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

        adapter.onItemClick = {
            findNavController().navigate(
                FastWillCountedListFragmentDirections.actionFastWillCountedListFragmentToEditedQuantityDialog(
                    it.productDto.code,
                    it.newQuantity.get().toIntOrZero(),
                    it.productDto.id
                )
            )
        }
    }

    override fun subscribeToObservables() {
        viewModel.list.observe(this) {
            adapter.submitList(it)
        }

        setFragmentResultListener(EditedQuantityDialog.REQUEST_KEY) { _, bundle ->
            viewModel.updateProductQuantity(
                productId = bundle.getInt(EditedQuantityDialog.ARG_PRODUCT_ID),
                quantity = bundle.getInt(EditedQuantityDialog.ARG_PRODUCT_QUANTITY).orZero()
            )
        }
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        WillCountedProductListAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    companion object {
        const val REQUEST_KEY = "FastWillCountedListFragment"
        const val ARG_PRODUCT_LIST = "productList"
    }
}