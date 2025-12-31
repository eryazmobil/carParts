package eryaz.software.carParts.ui.dashboard.query.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.ProductAddressControlPointDto

class ControlPointProductQuantityAdapter :
    ListAdapter<ProductAddressControlPointDto, RecyclerView.ViewHolder>(DiffCallBackControlPoint) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ControlPointQuantityVH.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ControlPointQuantityVH -> {
                holder.bind(
                    dto = getItem(position),
                    isLastItem = position == itemCount.minus(1),
                )
            }
        }
    }
}

object DiffCallBackControlPoint : DiffUtil.ItemCallback<ProductAddressControlPointDto>() {
    override fun areItemsTheSame(
        oldItem: ProductAddressControlPointDto,
        newItem: ProductAddressControlPointDto
    ) = oldItem.code == newItem.code

    override fun areContentsTheSame(
        oldItem: ProductAddressControlPointDto,
        newItem: ProductAddressControlPointDto
    ) = oldItem == newItem
}