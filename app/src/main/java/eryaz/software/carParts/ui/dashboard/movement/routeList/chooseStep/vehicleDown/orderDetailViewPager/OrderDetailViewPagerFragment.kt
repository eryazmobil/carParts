package eryaz.software.carParts.ui.dashboard.movement.routeList.chooseStep.vehicleDown.orderDetailViewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import eryaz.software.carParts.databinding.FragmentOrderDetailViewpagerBinding
import eryaz.software.carParts.ui.base.BaseDialogFragment
import eryaz.software.carParts.util.extensions.removeItemAnimator
import eryaz.software.carParts.util.extensions.removeOverScroll
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OrderDetailViewPagerFragment : BaseDialogFragment() {

    private val safeArgs by navArgs<OrderDetailViewPagerFragmentArgs>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentOrderDetailViewpagerBinding.inflate(layoutInflater)
    }

    override val viewModel by viewModel<OrderDetailViewPagerVM> {
        parametersOf(safeArgs.shippingRouteId, safeArgs.orderHeaderId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun subscribeToObservables() {

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Paket içeriği"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sipariş içeriği"))

        binding.viewPager2.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private val adapter by lazy {
        PackageDetailViewPagerAdapter(
            this,
            safeArgs.shippingRouteId,
            safeArgs.orderHeaderId).also {
            binding.viewPager2.adapter = it
            binding.viewPager2.removeOverScroll()
            binding.viewPager2.removeItemAnimator()
            binding.viewPager2.isUserInputEnabled = false
        }
    }
}