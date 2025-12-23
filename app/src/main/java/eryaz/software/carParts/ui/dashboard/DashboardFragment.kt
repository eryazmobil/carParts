package eryaz.software.carParts.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eryaz.software.carParts.data.R
import eryaz.software.carParts.databinding.FragmentDashboardBinding
import eryaz.software.carParts.data.enums.DashboardPermissionType.RETURNING
import eryaz.software.carParts.data.enums.IconType
import eryaz.software.carParts.data.persistence.SessionManager.clearData
import eryaz.software.carParts.ui.base.BaseFragment
import eryaz.software.carParts.util.adapter.dashboard.adapters.DashboardAdapter
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener
import eryaz.software.carParts.util.dialogs.QuestionDialog
import eryaz.software.carParts.util.extensions.onBackPressedCallback
import eryaz.software.carParts.util.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment() {
    override val viewModel by viewModel<DashboardViewModel>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentDashboardBinding.inflate(layoutInflater)
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

    override fun subscribeToObservables() {
        viewModel.dashboardItemList
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
    }

    private val adapter by lazy {
        DashboardAdapter().also {
            it.also {
                binding.recyclerView.adapter = it
            }
        }
    }

    override fun setClicks() {

        adapter.onItemClick = { dto ->
            when (dto.type) {

                RETURNING -> toast(getString(R.string.not_done_yet))

                else -> {
                    val title = requireActivity().getString(dto.titleRes)
                    findNavController().navigate(
                        DashboardFragmentDirections.actionMainFragmentToDashboardDetailFragment(
                            title, dto.type
                        )
                    )
                }
            }
        }

        binding.settingsBtn.setOnSingleClickListener {
            findNavController().navigate(
                DashboardFragmentDirections.actionMainFragmentToNavSettings()
            )
        }

        onBackPressedCallback {
            QuestionDialog(
                onNegativeClickListener = {
                },
                onPositiveClickListener = {
                    clearData()
                    findNavController().navigateUp()
                },
                textHeader = resources.getString(R.string.exit),
                textMessage = resources.getString(R.string.are_you_sure),
                positiveBtnText = resources.getString(R.string.yes),
                negativeBtnText = resources.getString(R.string.no),
                singleBtnText = "",
                negativeBtnViewVisible = true,
                icType = IconType.Success.ordinal
            ).show(parentFragmentManager, "dialog")
        }
    }

    override fun hideActionBar() = true
}