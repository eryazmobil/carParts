package eryaz.software.carParts.ui.dashboard.query.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.ProductShelfQuantityDto
import eryaz.software.carParts.databinding.ItemStorageQuantityTextBinding

class ProductQuantityVH(val binding: ItemStorageQuantityTextBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: ProductShelfQuantityDto,
        isLastItem: Boolean
    ) {
        binding.keyTxt.text = dto.product.code
        binding.valueTxt.text = dto.quantity

        binding.underline.isVisible = !isLastItem
    }

    companion object {
        fun from(parent: ViewGroup): ProductQuantityVH {
            val binding = ItemStorageQuantityTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ProductQuantityVH(binding)
        }
    }
}