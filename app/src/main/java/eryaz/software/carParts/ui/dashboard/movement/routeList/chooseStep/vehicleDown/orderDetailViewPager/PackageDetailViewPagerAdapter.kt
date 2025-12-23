package eryaz.software.carParts.ui.dashboard.movement.routeList.chooseStep.vehicleDown.orderDetailViewPager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import eryaz.software.carParts.ui.dashboard.movement.routeList.chooseStep.vehicleDown.orderDetailViewPager.orderDetail.OrderDetailFragment
import eryaz.software.carParts.ui.dashboard.movement.routeList.chooseStep.vehicleDown.orderDetailViewPager.packageDetail.PackageDetailFragment
import eryaz.software.carParts.util.extensions.newInstance

class PackageDetailViewPagerAdapter(
    parent: Fragment,
    val shippingRouteId: Int,
    val orderHeaderId: Int
) : FragmentStateAdapter(parent) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            newInstance<PackageDetailFragment>(
                PackageDetailFragment.SHIPPING_ROUTE_ID to shippingRouteId,
                PackageDetailFragment.ORDER_HEADER_ID to orderHeaderId
            )
        } else {
            newInstance<OrderDetailFragment>(
                OrderDetailFragment.ORDER_HEADER_ID to orderHeaderId
            )
        }
    }
}
