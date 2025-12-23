package eryaz.software.carParts.ui.dashboard.inbound.placement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.databinding.FragmentPlacementListBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.adapter.inbound.adapter.WorkActivityAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlacementListFragment : BaseFragment() {
    override val viewModel by viewModel<PlacementListVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentPlacementListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.placementVM = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.getPlacementList()
    }

    override fun setClicks() {

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter.onItemClick = { workActivity ->
            TemporaryCashManager.getInstance().workActivity = workActivity
            viewModel.getWorkAction()
        }
    }

    override fun subscribeToObservables() {

        viewModel.placementWorkActivityList
            .observe(viewLifecycleOwner) { placementList ->
                adapter.submitList(placementList)
            }

        viewModel.searchList()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

        viewModel.workActionDto
            .asLiveData()
            .observe(viewLifecycleOwner) {
                findNavController().navigate(
                    PlacementListFragmentDirections.actionPlacementListFragmentToPlacementDetailFragment()
                )
            }

    }

    private val adapter by lazy {
        WorkActivityAdapter().also {
            binding.recyclerView.adapter = it
        }
    }
}