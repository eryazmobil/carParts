package eryaz.software.carParts.ui.dashboard.query.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.dto.PackageDetailDto
import eryaz.software.carParts.databinding.ItemPackageDetailBinding

class ItemPackageDetailVH(val binding: ItemPackageDetailBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: PackageDetailDto
    ) {
        binding.dto = dto
    }

    companion object {
        fun from(parent: ViewGroup): ItemPackageDetailVH {
            val binding = ItemPackageDetailBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ItemPackageDetailVH(binding)
        }
    }
}