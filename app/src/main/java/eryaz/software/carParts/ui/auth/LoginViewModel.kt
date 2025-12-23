package eryaz.software.carParts.ui.auth

import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.enums.UiState
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.remote.request.LoginRequest
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.data.repositories.AuthRepo
import eryaz.software.carParts.data.repositories.UserRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import eryaz.software.carParts.util.CombinedStateFlow
import eryaz.software.carParts.util.extensions.isValidPassword
import eryaz.software.carParts.util.extensions.isValidUserId
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class LoginViewModel(
    private val authRepo: AuthRepo,
    private val userRepo: UserRepo
) : BaseViewModel() {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _navigateToMain = MutableSharedFlow<Boolean>()
    val navigateToMain = _navigateToMain.asSharedFlow()

    val loginEnable = CombinedStateFlow(email, password) {
        email.value.isValidUserId() && password.value.isValidPassword()
    }

    init {
        _uiState.value = UiState.EMPTY
    }

    fun login() = executeInBackground(_uiState, hasNextRequest = true) {
        val request = LoginRequest(
            email = email.value,
            password = password.value
        )

        authRepo.login(request).onSuccess {
            SessionManager.token = it.accessToken

            fetchWorkActionTypeList()
        }.onError { message, _ ->
            showError(
                ErrorDialogDto(
                    titleRes = R.string.error,
                    message = message
                )
            )
        }
    }

    private fun fetchWorkActionTypeList() = executeInBackground(_uiState) {
        userRepo.fetchWorkActionTypeList().onSuccess {
            TemporaryCashManager.getInstance().workActionTypeList = it
            _navigateToMain.emit(true)
        }
    }
}



