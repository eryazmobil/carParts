package eryaz.software.carParts.ui.dashboard.query.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.ProductShelfQuantityDto
import eryaz.software.carParts.databinding.ItemStorageQuantityTextBinding
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener

class ShelfProductQuantityVH(val binding: ItemStorageQuantityTextBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: ProductShelfQuantityDto,
        isLastItem: Boolean,
        onCopyClick: (shelfAddress: String) -> Unit
    ) {
        binding.keyTxt.text = dto.shelf?.shelfAddress
        binding.valueTxt.text = dto.quantity
        binding.underline.isVisible = !isLastItem
        binding.copyButton.setOnSingleClickListener {
            onCopyClick(binding.keyTxt.text.toString())
        }
    }

    companion object {
        fun from(parent: ViewGroup): ShelfProductQuantityVH {
            val binding = ItemStorageQuantityTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ShelfProductQuantityVH(binding)
        }
    }
}