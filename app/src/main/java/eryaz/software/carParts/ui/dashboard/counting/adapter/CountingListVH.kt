package eryaz.software.carParts.ui.dashboard.counting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.StockTakingHeaderDto
import eryaz.software.carParts.databinding.ItemCountingWorkActivityListBinding
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener

class CountingListVH(val binding: ItemCountingWorkActivityListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: StockTakingHeaderDto,
        onItemClick: ((StockTakingHeaderDto) -> Unit)
    ) {
        binding.dto = dto

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }
    }

    companion object {
        fun from(parent: ViewGroup): eryaz.software.carParts.ui.dashboard.counting.adapter.CountingListVH {
            val binding = ItemCountingWorkActivityListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return CountingListVH(binding)
        }
    }
}