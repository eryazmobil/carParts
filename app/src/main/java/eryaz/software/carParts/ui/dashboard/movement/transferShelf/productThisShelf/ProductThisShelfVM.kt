package eryaz.software.carParts.ui.dashboard.movement.transferShelf.productThisShelf

import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.ProductShelfQuantityDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductThisShelfVM(
    val repo: WorkActivityRepo,
    val productId: Int
) : BaseViewModel() {

    private val _shelfList = MutableStateFlow(listOf<ProductShelfQuantityDto>())
    val shelfList = _shelfList.asStateFlow()

    init {
        getProductShelfQuantityList()
    }

    fun getProductShelfQuantityList() =
        executeInBackground(showProgressDialog = true) {

            repo.getProductShelfQuantityList(
                productId = productId,
                warehouseId = SessionManager.warehouseId,
                storageId = 0,
                companyId = SessionManager.companyId,
                shelfId = 0
            ).onSuccess {
                _shelfList.emit(it)
            }
        }

}