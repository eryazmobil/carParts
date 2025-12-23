package eryaz.software.carParts.data.models.remote.response

import androidx.databinding.ObservableField
import eryaz.software.carParts.data.enums.LanguageType

data class LanguageModel(
    val lang: LanguageType,
    val isSelected: ObservableField<Boolean>
)