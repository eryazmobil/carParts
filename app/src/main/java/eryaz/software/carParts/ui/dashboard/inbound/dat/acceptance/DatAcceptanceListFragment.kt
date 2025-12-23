package eryaz.software.carParts.ui.dashboard.inbound.dat.acceptance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.databinding.FragmentDatAcceptanceListBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.adapter.inbound.adapter.WorkActivityAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DatAcceptanceListFragment : BaseFragment() {
    override val viewModel by viewModel<DatAcceptanceListVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentDatAcceptanceListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.acceptanceViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.getWaybillWorkActivityList()
    }

    override fun subscribeToObservables() {
        viewModel.acceptanceList
            .observe(viewLifecycleOwner) { workActivity ->
                adapter.submitList(workActivity)
            }

        viewModel.searchList()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

        viewModel.workActionDto
            .asLiveData()
            .observe(viewLifecycleOwner) {
                findNavController().navigate(
                    DatAcceptanceListFragmentDirections
                        .actionDatAcceptanceListFragmentToDatAcceptanceProcessFragment()
                )
            }
    }

    override fun setClicks() {

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter.onItemClick= {
            TemporaryCashManager.getInstance().workActivity = it
            viewModel.getWorkActionForPda()
        }
    }

    private val adapter by lazy {
        WorkActivityAdapter().also {
            binding.recyclerView.adapter = it
        }
    }
}