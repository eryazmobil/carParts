package eryaz.software.carParts.ui.dashboard.movement.transferStockCorrection.storageList

import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.StorageDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.repositories.UserRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StorageListDialogVM(
    private val userRepo: UserRepo,
) : BaseViewModel() {

    private val _storageList = MutableStateFlow(listOf<StorageDto>())
    val storageList = _storageList.asStateFlow()

    init {
        getStorageListByWarehouse()
    }

    fun getStorageListByWarehouse() = executeInBackground(_uiState) {
        userRepo.getStorageListByWarehouse(
            warehouseId = SessionManager.warehouseId
        ).onSuccess {
            _storageList.emit(it)
        }
    }
}