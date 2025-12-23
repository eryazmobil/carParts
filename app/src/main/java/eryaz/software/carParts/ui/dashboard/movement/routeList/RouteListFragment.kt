package eryaz.software.carParts.ui.dashboard.movement.routeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.databinding.FragmentRouteListBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.adapter.movement.route.RouteAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class RouteListFragment : BaseFragment() {
    override val viewModel by viewModel<RouteListVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentRouteListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.routeListVM = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.fetchRouteList()
    }

    override fun subscribeToObservables() {

        viewModel.routeList
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

        viewModel.searchList()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
    }

    override fun setClicks() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter.onItemClick = {
            findNavController().navigate(
                RouteListFragmentDirections.actionRouteListFragmentToChooseVehicleFragment(
                    it.id
                )
            )
        }
    }

    private val adapter by lazy {
        RouteAdapter().also {
            binding.recyclerView.adapter = it
        }
    }
}