package eryaz.software.carParts.util.adapter.inbound.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.WorkActivityDto
import eryaz.software.carParts.databinding.ItemWorkActivityListBinding
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener

class WorkActivityViewHolder(val binding: ItemWorkActivityListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: WorkActivityDto,
        onItemClick: ((WorkActivityDto) -> Unit)
    ) {
        binding.dto = dto

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }
    }

    companion object {
        fun from(parent: ViewGroup): WorkActivityViewHolder {
            val binding = ItemWorkActivityListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return WorkActivityViewHolder(binding)
        }
    }
}