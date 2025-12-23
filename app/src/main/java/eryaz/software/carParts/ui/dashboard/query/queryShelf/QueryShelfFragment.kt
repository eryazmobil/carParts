package eryaz.software.carParts.ui.dashboard.query.queryShelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.databinding.FragmentQueryShelfBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.ui.dashboard.query.adapter.ProductQuantityAdapter
import eryaz.software.carParts.ui.dashboard.query.adapter.VarietyProductAdapter
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

class QueryShelfFragment : BaseFragment() {
    override val viewModel by viewModel<QueryShelfFragmentVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentQueryShelfBinding.inflate(layoutInflater)
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

        binding.searchEdt.setOnEditorActionListener { _, actionId, _ ->
            val isValidBarcode = viewModel.searchShelf.value.trim().isNotEmpty()

            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) || actionId == EditorInfo.IME_ACTION_DONE && isValidBarcode) {
                viewModel.getShelfByCode()
            }

            hideSoftKeyboard()
            true
        }
    }

    override fun subscribeToObservables() {
        viewModel.productList.asLiveData().observe(viewLifecycleOwner) {
                shelfAdapter.submitList(it)
            }

        viewModel.varietyShelf.asLiveData().observe(viewLifecycleOwner) {
                varietyAdapter.submitList(it)
            }
    }

    private val varietyAdapter by lazy(LazyThreadSafetyMode.NONE) {
        VarietyProductAdapter().also {
            binding.recyclerViewForVariety.adapter = it
        }
    }

    private val shelfAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ProductQuantityAdapter().also {
            binding.recyclerViewForShelf.adapter = it
        }
    }

    override fun onStart() {
        super.onStart()

        binding.searchEdt.requestFocus()
    }

    override fun hideActionBar() = false
}