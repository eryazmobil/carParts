package eryaz.software.carParts.util.adapter.inbound.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.databinding.ItemDiaglogBinding
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener

class ProductListViewHolder(val binding: ItemDiaglogBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: ProductDto,
        onItemClick: ((ProductDto) -> Unit)
    ) {

        binding.itemText.text = dto.code

        binding.root.setOnSingleClickListener {
            onItemClick(dto)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ProductListViewHolder {
            val binding = ItemDiaglogBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ProductListViewHolder(binding)
        }
    }
}