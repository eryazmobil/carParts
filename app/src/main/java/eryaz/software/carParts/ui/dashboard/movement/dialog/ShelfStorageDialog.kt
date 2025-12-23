package eryaz.software.carParts.ui.dashboard.movement.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import eryaz.software.carParts.R
import eryaz.software.carParts.data.models.dto.ButtonDto
import eryaz.software.carParts.data.models.dto.WarningDialogDto
import eryaz.software.carParts.databinding.DialogShelfStorageBinding
import eryaz.software.carParts.ui.base.BaseDialogFragment
import eryaz.software.carParts.util.adapter.movement.shelfStorageDialog.ShelfStorageAdapter
import eryaz.software.carParts.util.extensions.copyToClipboard
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ShelfStorageDialog : BaseDialogFragment() {

    private val navArgs: ShelfStorageDialogArgs by navArgs()

    override val viewModel by viewModel<ShelfStorageDialogVM>() {
        parametersOf(navArgs.productId)
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        DialogShelfStorageBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun subscribeToObservables() {
        val displayMetrics = resources.displayMetrics
        val widthPercentage = 1
        val heightPercentage = 0.6

        val width = (displayMetrics.widthPixels * widthPercentage).toInt()
        val height = (displayMetrics.heightPixels * heightPercentage).toInt()

        dialog?.window?.setLayout(width, height)

        viewModel.getProductShelfQuantityList { shelfStorageList ->
            adapter.submitList(shelfStorageList)
            if (shelfStorageList.isEmpty()) {
                warningDialog.show(
                    requireContext(),
                    WarningDialogDto(
                        titleRes = R.string.warning,
                        messageRes = R.string.no_shelf_belong_this_product,
                        completeButton = ButtonDto(
                            onClickListener = {
                                dialog?.dismiss()
                            }
                        )

                    )
                )
            }
        }

        adapter.onCopyButtonClickListener = { shelfAddress ->
            shelfAddress?.let {
                context?.copyToClipboard(it)
                Toast.makeText(context, "Kopyalandı", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ShelfStorageAdapter().also {
            binding.recyclerView.adapter = it
        }
    }

}