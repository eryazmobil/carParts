package eryaz.software.carParts.ui.dashboard.movement.routeList.chooseStep.vehicleDown.orderDetailViewPager.orderDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import eryaz.software.carParts.databinding.FragmentPackageDetailBinding
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.adapter.movement.packageList.PackageProductAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OrderDetailFragment : BaseFragment(){

    override val viewModel by viewModel<OrderDetailVM> {
        val orderHeaderId = arguments?.getInt(ORDER_HEADER_ID)

        parametersOf(orderHeaderId)
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
        viewModel.fetchOrderHeaderRouteDetailList()

        viewModel.orderList
            .asLiveData()
            .observe(this){
                adapter.submitList(it)
            }
    }

    private val adapter by lazy {
        PackageProductAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

    companion object {
        const val ORDER_HEADER_ID = "ORDER_HEADER_ID"
    }
}