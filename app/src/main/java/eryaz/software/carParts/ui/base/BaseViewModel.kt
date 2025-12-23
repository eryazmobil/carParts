package eryaz.software.carParts.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eryaz.software.carParts.data.api.utils.Resource
import eryaz.software.carParts.data.api.utils.asErrorDialogDto
import eryaz.software.carParts.data.api.utils.asUiState
import eryaz.software.carParts.data.enums.UiState
import eryaz.software.carParts.data.models.dto.ConfirmationDialogDto
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.WarningDialogDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _showErrorDialog = MutableStateFlow<ErrorDialogDto?>(null)
    val showErrorDialog = _showErrorDialog.asStateFlow()

    private val _showProgressDialog = MutableStateFlow<UiState?>(null)
    val showProgressDialog = _showProgressDialog.asStateFlow()

    private val _showWarningDialog = MutableStateFlow<WarningDialogDto?>(null)
    val showWarningDialog = _showWarningDialog.asStateFlow()

    private val _showConfirmationDialog = MutableStateFlow<ConfirmationDialogDto?>(null)
    val showConfirmationDialog = _showConfirmationDialog.asStateFlow()

    protected val _uiState = MutableStateFlow(UiState.LOADING)
    val uiState = _uiState.asStateFlow()

    var stringProvider: (Int) -> String = { "" }

    fun <T> executeInBackground(
        uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.LOADING),
        checkEmptyList: Boolean = false,
        hasNextRequest: Boolean = false,
        checkErrorState: Boolean = true,
        showErrorDialog: Boolean = true,
        showProgressDialog: Boolean = false,
        func: suspend () -> Resource<T>
    ) {
        if (uiState.value != UiState.LOADING)
            uiState.value = UiState.LOADING

        if (showProgressDialog)
            _showProgressDialog.value = uiState.value

        viewModelScope.launch {
            val response = func()
            val newState = response.asUiState(checkEmptyList)

            if (showErrorDialog && newState == UiState.ERROR)
                showError(response.asErrorDialogDto())

            if (hasNextRequest && newState == UiState.SUCCESS)
                return@launch

            if (checkErrorState || newState != UiState.ERROR)
                uiState.value = newState

            if (showProgressDialog)
                _showProgressDialog.value = uiState.value
        }
    }

    fun showError(model: ErrorDialogDto?) {
        _showErrorDialog.value = model
    }

    fun showWarning(model: WarningDialogDto?) {
        _showWarningDialog.value = model
    }

    fun showConfirmation(model: ConfirmationDialogDto?) {
        _showConfirmationDialog.value = model
    }

    fun showProgressDialog(show: Boolean) {
        viewModelScope.launch {
            delay(100)

            _showProgressDialog.emit(if (show) UiState.LOADING else UiState.ERROR)
        }
    }
}