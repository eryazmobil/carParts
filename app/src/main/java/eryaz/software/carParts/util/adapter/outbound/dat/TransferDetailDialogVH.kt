package eryaz.software.carParts.util.adapter.outbound.dat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.TransferRequestDetailDto
import eryaz.software.carParts.databinding.ItemTransferDetailListBinding

class TransferDetailDialogVH(val binding: ItemTransferDetailListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(dto: TransferRequestDetailDto) {
        binding.dto = dto
        binding.executePendingBindings()
        setControl(dto)
    }

    private fun setControl(dto: TransferRequestDetailDto) {
        val (statusName, statusColor) = when {
            dto.quantityPicked == dto.quantity -> "TAM" to "#4caf50"
            dto.quantityPicked < dto.quantity -> "EKSIK" to "#df0029"
            else -> "FAZLA" to "#ffb822"
        }

        binding.controlTitle.apply {
            text = statusName
            setBackgroundColor(Color.parseColor(statusColor))
        }
    }

    companion object {
        fun from(parent: ViewGroup): TransferDetailDialogVH {
            val binding = ItemTransferDetailListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return TransferDetailDialogVH(binding)
        }
    }
}