package eryaz.software.carParts.ui.dashboard.movement.dialog

import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.mappers.toShelfStorageModel
import eryaz.software.carParts.data.models.remote.models.ShelfStorageModel
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShelfStorageDialogVM(
    private val repo: WorkActivityRepo,
    private val productId: Int,
) : BaseViewModel() {

    private val _shelfStorageList = MutableStateFlow(listOf<ShelfStorageModel>())
    val shelfStorageList = _shelfStorageList.asStateFlow()

    fun getProductShelfQuantityList(onSuccess: (List<ShelfStorageModel>) -> Unit) =
        executeInBackground(showProgressDialog = true) {

            repo.getProductShelfQuantityList(
                productId = productId,
                warehouseId = SessionManager.warehouseId,
                storageId = 0,
                companyId = SessionManager.companyId,
                shelfId = 0
            ).onSuccess {
                _shelfStorageList.emit(it.map { productShelfQuantityDto ->
                    productShelfQuantityDto.toShelfStorageModel()
                })
                onSuccess(_shelfStorageList.value)
            }
        }
}