package eryaz.software.carParts.ui.dashboard.inbound.acceptance.acceptanceProcess.waybillDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eryaz.software.carParts.databinding.WaybillDetailDialogListBinding
import eryaz.software.carParts.ui.base.BaseDialogFragment
import eryaz.software.carParts.util.adapter.inbound.adapter.WaybillDetailListDialogAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class WaybillDetailListDialog : BaseDialogFragment() {

    override val viewModel by viewModel<WaybillDialogVM>()

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        WaybillDetailDialogListBinding.inflate(layoutInflater)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.waybillDetailList
            .observe(viewLifecycleOwner) { waybillDetailList ->
                adapter.submitList(waybillDetailList)
            }

        viewModel.searchList()
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
    }

    private val adapter by lazy {
        WaybillDetailListDialogAdapter().also {
            binding.recyclerView.adapter = it
        }
    }
}