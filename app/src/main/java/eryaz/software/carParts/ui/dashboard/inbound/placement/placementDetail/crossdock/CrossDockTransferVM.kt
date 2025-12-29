package eryaz.software.carParts.ui.dashboard.inbound.placement.placementDetail.crossdock

import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.CrossDockCheckDto
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import eryaz.software.carParts.util.extensions.orZero
import eryaz.software.carParts.util.extensions.toDoubleOrZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CrossDockTransferVM(
    val repo: WorkActivityRepo,
    val productDto: ProductDto,
    val maxAmount: Int
) : BaseViewModel() {
    val quantity = MutableStateFlow("")
    val note = MutableStateFlow("")

    var crossDockDto: CrossDockCheckDto? = null

    private val _code = MutableStateFlow("")
    val code = _code.asStateFlow()

    private val _quantityCross = MutableStateFlow("")
    val quantityCross = _quantityCross.asStateFlow()

    val maxQuantity = maxAmount.toString()

    private val _navigateToBack = MutableStateFlow(false)
    val navigateToBack = _navigateToBack.asStateFlow()

    init {
        checkCrossDock()
    }

    fun checkCrossDock() {
        TemporaryCashManager.getInstance().workAction?.let {
            executeInBackground(_uiState) {
                repo.getCrossDockRequestForPlacing(
                    productId = productDto.id,
                    companyId = SessionManager.companyId,
                    warehouseId = SessionManager.warehouseId
                ).onSuccess {
                    crossDockDto = it

                    _code.emit(crossDockDto?.orderHeader?.controlPoint?.code + " - " + crossDockDto?.orderHeader?.client?.code)
                    _quantityCross.emit(it.quantity)

                }
            }
        }
    }

    fun updateWaybillControlAddQuantity() {
        TemporaryCashManager.getInstance().workAction?.let {
            executeInBackground(showErrorDialog = true) {
                repo.updateWaybillPlacementAddQuantity(
                    actionId = it.workActionId,
                    productId = productDto.id,
                    quantity = quantity.value.toDoubleOrZero().toInt(),
                    shelfId = 0,
                    containerId = 0,
                    crossDockId = crossDockDto?.id.orZero(),
                    note = note.value
                ).onSuccess {
                    _navigateToBack.emit(true)
                }
            }
        }
    }
}