package eryaz.software.carParts.ui.dashboard.settings.changeLanguage

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import eryaz.software.carParts.data.enums.LanguageType
import eryaz.software.carParts.data.models.remote.response.LanguageModel
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LanguageVM : BaseViewModel() {

    private val _langList = MutableStateFlow<List<LanguageModel>>(emptyList())
    val langList = _langList.asStateFlow()

    init {
        getLangList()
    }

    private fun getLangList() {
        viewModelScope.launch {
            val model = LanguageType.entries.map {
                LanguageModel(
                    lang = it,
                    isSelected = ObservableField(it == SessionManager.language)
                )
            }

            _langList.emit(model)
        }
    }
}
