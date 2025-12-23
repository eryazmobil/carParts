package eryaz.software.carParts.ui.dashboard.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.WarehouseDto
import eryaz.software.carParts.databinding.ItemDiaglogBinding
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener

class WarehouseViewHolder(val binding: ItemDiaglogBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: WarehouseDto,
        onItemClickWarehouse: (WarehouseDto) -> Unit
    ) {

        with(binding) {
            binding.itemText.text = dto.name

            root.setOnSingleClickListener {
                onItemClickWarehouse.invoke(dto)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): WarehouseViewHolder {
            val binding = ItemDiaglogBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return WarehouseViewHolder(binding)
        }
    }
}