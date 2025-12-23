package eryaz.software.carParts.ui.dashboard.counting.firstCounting

import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.StockTakingHeaderDto
import eryaz.software.carParts.data.models.dto.WarningDialogDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.repositories.CountingRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FirstCountingListVM(private val repo: CountingRepo) : BaseViewModel() {

    private val _countingWorkActivityList = MutableStateFlow(listOf<StockTakingHeaderDto>())
    val countingWorkActivityList = _countingWorkActivityList.asStateFlow()

    val stockTackingHeaderId :Int = 0
    val countingWorkActivityId :Int = 0

    fun fetchCountingWorkActivityList() {
        executeInBackground(_uiState) {
            repo.getCountingWorkActivityList(
                companyId = SessionManager.companyId,
                warehouseId = SessionManager.warehouseId
            ).onSuccess {
                if (it.isEmpty()) {
                    _countingWorkActivityList.emit(emptyList())
                    showWarning(
                        WarningDialogDto(
                            title = stringProvider.invoke(R.string.not_found_work_activity),
                            message = stringProvider.invoke(R.string.list_is_empty)
                        )
                    )
                }else {
                    _countingWorkActivityList.emit(it)
                }
            }.onError { _, _ ->
                _countingWorkActivityList.emit(emptyList())
            }
        }
    }
    companion object {
       const val FIRST_COUNTING_WAREHOUSE = 7
    }
}
