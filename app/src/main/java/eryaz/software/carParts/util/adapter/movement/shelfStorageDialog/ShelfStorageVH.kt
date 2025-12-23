package eryaz.software.carParts.util.adapter.movement.shelfStorageDialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eryaz.software.carParts.data.models.remote.models.ShelfStorageModel
import eryaz.software.carParts.databinding.ItemRowShelfStorageQtyBinding

class ShelfStorageVH(val binding: ItemRowShelfStorageQtyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        dto: ShelfStorageModel,
        onCopyButtonClickListener: (String?) -> Unit
    ) {
        binding.dto = dto
       binding.copyBtn.setOnClickListener {
           onCopyButtonClickListener(dto.shelfDto?.shelfAddress)
       }
    }

    companion object {
        fun from(parent: ViewGroup): ShelfStorageVH {
            val binding = ItemRowShelfStorageQtyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ShelfStorageVH(binding)
        }
    }
}