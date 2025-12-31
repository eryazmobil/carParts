package eryaz.software.carParts.ui.dashboard.inbound.acceptance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.enums.ActionType
import eryaz.software.carParts.data.models.dto.WarningDialogDto
import eryaz.software.carParts.data.models.dto.WorkActionDto
import eryaz.software.carParts.data.models.dto.WorkActivityDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import eryaz.software.carParts.util.extensions.orZero
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AcceptanceListVM(private val repo: WorkActivityRepo) : BaseViewModel() {

    private val _acceptanceList = MutableLiveData<List<WorkActivityDto?>>(emptyList())
    val acceptanceList: LiveData<List<WorkActivityDto?>> = _acceptanceList

    val search = MutableLiveData("")

    private val _workActionDto = MutableSharedFlow<WorkActionDto>()
    val workActionDto = _workActionDto.asSharedFlow()

    fun searchList() = search.switchMap { query ->
        MutableLiveData<List<WorkActivityDto?>>().apply {
            value = filterData(query)
        }
    }
    private fun filterData(query: String): List<WorkActivityDto?> {
        val dataList = _acceptanceList.value.orEmpty()

        if (query.isEmpty()) return dataList

        return dataList.filter { data ->
            val nameMatch = data?.client?.name?.contains(query, ignoreCase = true) ?: false
            val codeMatch = data?.client?.code?.contains(query, ignoreCase = true) ?: false

            nameMatch || codeMatch
        }
    }

    fun getWaybillWorkActivityList() {
        executeInBackground(_uiState) {
            repo.getWaybillWorkActivityList(
                SessionManager.companyId,
                SessionManager.warehouseId
            ).onSuccess {
                if (it.isEmpty()) {
                    _acceptanceList.value = emptyList()
                    showWarning(
                        WarningDialogDto(
                            title = stringProvider.invoke(R.string.not_found_work_activity),
                            message = stringProvider.invoke(R.string.list_is_empty)
                        )
                    )
                }
                _acceptanceList.value = it

            }.onError { _, _ ->
                _acceptanceList.value = emptyList()

            }
        }
    }

    fun getWorkActionForPda() {
        executeInBackground(
            _uiState,
            showErrorDialog = false,
            checkErrorState = false
        ) {
            repo.getWorkActionForPda(
                userId = SessionManager.userId,
                workActivityId = TemporaryCashManager.getInstance().workActivity?.workActivityId.orZero(),
                actionTypeId =    TemporaryCashManager.getInstance().workActionTypeList?.find { model -> model.code == "Control" }?.id.orZero()
            ).onSuccess {
                _workActionDto.emit(it)
                TemporaryCashManager.getInstance().workAction = it
            }.onError { _, _ ->
                createWorkAction()
            }
        }
    }

    private fun createWorkAction() {
        executeInBackground {
            repo.createWorkAction(
                activityId = TemporaryCashManager.getInstance().workActivity?.workActivityId ?: 0,
                actionTypeCode = ActionType.CONTROL.type
            ).onSuccess {
                TemporaryCashManager.getInstance().workAction = it
                _workActionDto.emit(it)
            }
        }
    }
}