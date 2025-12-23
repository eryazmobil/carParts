package eryaz.software.carParts.ui.dashboard.movement.routeList.chooseStep.vehicleDown.orderDetailViewPager.packageDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import eryaz.software.carParts.databinding.FragmentPackageDetailBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.adapter.movement.route.RouteAdapter
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class PackageDetailFragment : BaseFragment() {

    override val viewModel by viewModel<PackageDetailVM> {
        val shippingRouteId = arguments?.getInt(SHIPPING_ROUTE_ID)
        val orderHeaderId = arguments?.getInt(ORDER_HEADER_ID)

        parametersOf(shippingRouteId, orderHeaderId)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentPackageDetailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun subscribeToObservables() {
        viewModel.getOrderHeaderRouteDetailList()

        viewModel.packageList
            .asLiveData()
            .observe(this) {
                adapter.submitList(it)
            }
    }

    private val adapter by lazy {
        RouteAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    companion object {
        const val SHIPPING_ROUTE_ID = "SHIPPING_ROUTE_ID"
        const val ORDER_HEADER_ID = "ORDER_HEADER_ID"
    }
}