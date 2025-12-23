package eryaz.software.carParts.data.api.utils

import eryaz.software.carParts.data.enums.ResponseStatus
import eryaz.software.carParts.data.enums.UiState
import eryaz.software.carParts.data.models.dto.ErrorDialogDto

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()

    data class Error(
        val message: String? = "",
        val statusEnum: ResponseStatus = ResponseStatus.FAILED
    ) : Resource<Nothing>()

}

inline fun <T : Any> Resource<T>.onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) action(data)
    return this
}

inline fun <T : Any> Resource<T>.onError(action: (message: String?, statusEnum: ResponseStatus) -> Unit): Resource<T> {
    if (this is Resource.Error) action(message, statusEnum)
    return this
}

fun <T> Resource<T>.asUiState(checkEmptyList: Boolean = false): UiState {
    return when (this) {
        is Resource.Success -> if (checkEmptyList && this.data is List<*> && (this.data as List<*>).isNullOrEmpty()) UiState.EMPTY else UiState.SUCCESS
        is Resource.Error -> UiState.ERROR
    }
}

fun <T> Resource<T>.asErrorDialogDto(): ErrorDialogDto? {
    return when (this) {
        is Resource.Error -> ErrorDialogDto(message = this.message)
        else -> null
    }
}

