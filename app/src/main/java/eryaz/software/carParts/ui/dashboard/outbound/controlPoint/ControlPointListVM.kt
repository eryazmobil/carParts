package eryaz.software.carParts.ui.dashboard.outbound.controlPoint

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.ControlPointScreenDto
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.OrderHeaderDto
import eryaz.software.carParts.data.models.dto.WarningDialogDto
import eryaz.software.carParts.data.models.dto.WorkActivityDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.repositories.OrderRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.collections.filter
import kotlin.collections.orEmpty

class ControlPointListVM(private val repo: OrderRepo) : BaseViewModel() {

    val search = MutableLiveData("")
    var controlPointId: Int = 0

    private val _navigateToHeaderDialog = MutableSharedFlow<Boolean>()
    val navigateToHeaderDialog = _navigateToHeaderDialog.asSharedFlow()

    private val _controlPointList = MutableStateFlow(listOf<ControlPointScreenDto>())
    val controlPointList = _controlPointList.asSharedFlow()

    private val _orderHeaderList = MutableSharedFlow<List<OrderHeaderDto>>()
    val orderHeaderList = _orderHeaderList.asSharedFlow()


    fun searchList() = search.switchMap { query ->
        MutableLiveData<List<ControlPointScreenDto?>>().apply {
            value = filterData(query)
        }
    }

    private fun filterData(query: String): List<ControlPointScreenDto?> {
        val dataList = _controlPointList.value

        val filteredList = dataList.filter { data ->
            data.clientNames.contains(query, ignoreCase = true)
        }
        return filteredList
    }

    fun fetchControlPointList() {
        executeInBackground(_uiState) {
            repo.getControlPointList(
                warehouseId = SessionManager.warehouseId,
                type = PICKING_CONTROL_TYPE
            )
                .onSuccess {
                    val controlList = it.filter { dto ->
                        dto.status != "white"
                    }

                    if (controlList.isNotEmpty()) {
                        _controlPointList.emit(controlList)
                    } else {
                        showWarning(
                            WarningDialogDto(
                                title = stringProvider.invoke(R.string.not_found_work_activity),
                                message = stringProvider.invoke(R.string.list_is_empty)
                            )
                        )
                    }
                }.onError { message, _ ->
                    showError(
                        ErrorDialogDto(
                            titleRes = R.string.error,
                            message = message
                        )
                    )
                }
        }
    }

    fun getOrderHeaderListByControlPointId() {
        executeInBackground(showProgressDialog = true) {
            repo.getOrderHeaderListByControlPointId(controlPointId = controlPointId)
                .onSuccess {
                    if (it.isNotEmpty()) {
                        _orderHeaderList.emit(it)
                    }
                }.onError { message, _ ->
                    showError(
                        ErrorDialogDto(
                            titleRes = R.string.error,
                            message = message
                        )
                    )
                }
        }
    }

    companion object {
        const val PICKING_CONTROL_TYPE = 1
    }
}