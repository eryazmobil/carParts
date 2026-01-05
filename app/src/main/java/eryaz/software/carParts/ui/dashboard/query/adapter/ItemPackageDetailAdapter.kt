package eryaz.software.carParts.ui.dashboard.query.adapter


import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.PackageDetailDto

class ItemPackageDetailAdapter :
    ListAdapter<PackageDetailDto, RecyclerView.ViewHolder>(DiffCallPackageDetail) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemPackageDetailVH.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemPackageDetailVH -> {
                holder.bind(
                    dto = getItem(position)
                )
            }
        }
    }
}

object DiffCallPackageDetail : DiffUtil.ItemCallback<PackageDetailDto>() {
    override fun areItemsTheSame(
        oldItem: PackageDetailDto,
        newItem: PackageDetailDto
    ) = oldItem.packageId == newItem.packageId

    override fun areContentsTheSame(
        oldItem: PackageDetailDto,
        newItem: PackageDetailDto
    ) = oldItem == newItem
}