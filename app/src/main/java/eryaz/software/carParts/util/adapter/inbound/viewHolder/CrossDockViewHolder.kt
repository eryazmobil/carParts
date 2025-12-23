package eryaz.software.carParts.util.adapter.inbound.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.CrossDockDto
import eryaz.software.carParts.databinding.ItemCrossDockListBinding
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener

class CrossDockViewHolder(val binding: ItemCrossDockListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: CrossDockDto,
        onItemClick: ((CrossDockDto) -> Unit)
    ) {
        binding.dto = dto

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }
    }

    companion object {
        fun from(parent: ViewGroup): CrossDockViewHolder {
            val binding = ItemCrossDockListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return CrossDockViewHolder(binding)
        }
    }
}