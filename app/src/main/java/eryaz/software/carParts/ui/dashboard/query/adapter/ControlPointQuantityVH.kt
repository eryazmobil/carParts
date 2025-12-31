package eryaz.software.carParts.ui.dashboard.query.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.ProductAddressControlPointDto
import eryaz.software.carParts.data.models.dto.ProductShelfQuantityDto
import eryaz.software.carParts.databinding.ItemStorageQuantityControlPointTextBinding
import eryaz.software.carParts.databinding.ItemStorageQuantityTextBinding
import eryaz.software.carParts.util.bindingAdapter.setOnSingleClickListener

class ControlPointQuantityVH(val binding: ItemStorageQuantityControlPointTextBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: ProductAddressControlPointDto,
        isLastItem: Boolean,
    ) {
        binding.keyTxt.text = dto.code
        binding.valueTxt.text = dto.quantity
        binding.underline.isVisible = !isLastItem
    }

    companion object {
        fun from(parent: ViewGroup): ControlPointQuantityVH {
            val binding = ItemStorageQuantityControlPointTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ControlPointQuantityVH(binding)
        }
    }
}