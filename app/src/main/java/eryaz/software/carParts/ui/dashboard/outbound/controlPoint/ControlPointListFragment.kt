package eryaz.software.carParts.ui.dashboard.outbound.controlPoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.databinding.FragmentControlPointListBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.adapter.outbound.ControlPointListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ControlPointListFragment : BaseFragment() {
    override val viewModel by viewModel<ControlPointListVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentControlPointListBinding.inflate(layoutInflater)
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

    override fun onStart() {
        super.onStart()
        viewModel.fetchControlPointList()
    }

    override fun subscribeToObservables() {

        viewModel.controlPointList.asLiveData()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        
        viewModel.orderHeaderList
            .asLiveData()
            .observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    findNavController().navigate(
                        ControlPointListFragmentDirections.actionControlPointListFragmentToOrderHeaderListDialog(
                            list.toTypedArray()
                        )
                    )
                }
            }
    }

    override fun setClicks() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter.onItemClick = {
            viewModel.controlPointId = it.id
            viewModel.getOrderHeaderListByControlPointId()
        }
    }

    private val adapter by lazy {
        ControlPointListAdapter().also {
            binding.recyclerView.adapter = it
        }
    }
}