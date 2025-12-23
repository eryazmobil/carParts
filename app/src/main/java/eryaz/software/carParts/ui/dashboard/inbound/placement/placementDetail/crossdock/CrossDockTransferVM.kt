package eryaz.software.carParts.ui.dashboard.inbound.placement.placementDetail.crossdock

import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.CrossDockCheckDto
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import eryaz.software.carParts.util.extensions.toDoubleOrZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CrossDockTransferVM(
    val repo: WorkActivityRepo,
    val productDto: ProductDto,
    val crossDockDto: CrossDockCheckDto,
    val maxAmount: Int
) : BaseViewModel() {


    val quantity = MutableStateFlow("")

    val maxQuantity = maxAmount.toString()

    private val _navigateToBack = MutableStateFlow(false)
    val navigateToBack = _navigateToBack.asStateFlow()

    fun updateWaybillControlAddQuantity() {
        TemporaryCashManager.getInstance().workAction?.let {
            executeInBackground(showErrorDialog = true) {
                repo.updateWaybillPlacementAddQuantity(
                    actionId = it.workActionId,
                    productId = productDto.id,
                    quantity = quantity.value.toDoubleOrZero().toInt(),
                    shelfId = 0,
                    containerId = 0,
                    crossDockId = crossDockDto.id
                ).onSuccess {
                    _navigateToBack.emit(true)
                }
            }
        }
    }
}