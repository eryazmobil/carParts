package eryaz.software.carParts.util.adapter.movement.route

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.RouteDto
import eryaz.software.carParts.databinding.ItemRouteListBinding
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener

class RouteViewHolder(val binding: ItemRouteListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: RouteDto,
        onItemClick: ((RouteDto) -> Unit)
    ) {
        binding.dto = dto

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }
    }

    companion object {
        fun from(parent: ViewGroup): RouteViewHolder {
            val binding = ItemRouteListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return RouteViewHolder(binding)
        }
    }
}