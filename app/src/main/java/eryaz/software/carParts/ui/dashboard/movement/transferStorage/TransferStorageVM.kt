package eryaz.software.carParts.ui.dashboard.movement.transferStorage

import androidx.lifecycle.viewModelScope
import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.data.models.dto.StorageDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransferStorageVM(
    private val repo: WorkActivityRepo
) : BaseViewModel() {

    private var productID: Int = 0
    private var exitStorageId: Int = 0
    private var enterStorageId: Int = 0
    private var enterShelfEId: Int = 0

    val exitShelfId = MutableStateFlow(0)
    val transferSuccess = MutableStateFlow(false)
    val searchProduct = MutableStateFlow("")
    val exitShelfValue = MutableStateFlow("")
    val enterShelfValue = MutableStateFlow("")
    val enteredQuantity = MutableStateFlow("")

    private val _exitStorageName = MutableStateFlow("")
    val exitStorageName = _exitStorageName.asStateFlow()

    private val _enterStorageName = MutableStateFlow("")
    val enterStorageName = _enterStorageName.asStateFlow()

    private val _productDetail = MutableStateFlow<ProductDto?>(null)
    val productDetail = _productDetail.asStateFlow()

    private val _showProductDetail = MutableStateFlow(false)
    val showProductDetail = _showProductDetail.asStateFlow()

    fun getBarcodeByCode() = executeInBackground(
        showProgressDialog = true,
        showErrorDialog = false
    ) {
        repo.getBarcodeByCode(
            searchProduct.value.trim(),
            SessionManager.companyId
        ).onSuccess {
            productID = it.product.id
            _productDetail.emit(it.product)
            _showProductDetail.emit(true)
        }.onError { message, _ ->
            showError(
                ErrorDialogDto(
                    title = stringProvider.invoke(R.string.error),
                    message = message
                )
            )
        }
    }

    fun getShelfByCode(shelfCode: String, enterShelf: Boolean) {
        executeInBackground(showProgressDialog = true) {
            repo.getShelfByCode(
                code = shelfCode.trim(),
                warehouseId = SessionManager.warehouseId,
                storageId = 0
            ).onSuccess {
                if (enterShelf) {
                    enterShelfEId = it.shelfId
                } else {
                    exitShelfId.value = it.shelfId
                }
            }
        }
    }

    private fun isValidFields(): Boolean {
        when {
            exitStorageId == 0 -> {
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error,
                        messageRes = R.string.select_out_warehouse
                    )
                )
                return false
            }

            enterStorageId == 0 -> {
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error,
                        messageRes = R.string.select_in_warehouse
                    )
                )
                return false
            }

            enterShelfEId == 0 -> {
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error,
                        messageRes = R.string.enter_shelf_address_transfer
                    )
                )
                return false
            }

            enteredQuantity.value.toInt() == 0 -> {
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error,
                        messageRes = R.string.enter_valid_qty
                    )
                )
                return false
            }

            else ->
                return true
        }
    }

    fun createStorageMovement() {
        if (isValidFields()) {
            executeInBackground {
                repo.createStorageMovement(
                    productId = productID,
                    enterStorageId = enterStorageId,
                    exitStorageId = exitStorageId,
                    enterShelfId = enterShelfEId,
                    exitShelfId = exitShelfId.value,
                    quantity = enteredQuantity.value.toInt(),
                    warehouseId = SessionManager.warehouseId
                ).onSuccess {
                    transferSuccess.emit(true)
                    productID = 0
                    exitStorageId = 0
                    enterStorageId = 0
                    enterShelfEId = 0
                    exitShelfId.emit(0)

                    searchProduct.emit("")
                    _exitStorageName.emit("")
                    _enterStorageName.emit("")
                    exitShelfValue.emit("")
                    enterShelfValue.emit("")
                    enteredQuantity.emit("")
                    _productDetail.emit(null)
                    _showProductDetail.emit(false)
                }
            }
        }
    }

    fun setEnterStorage(storageDtoEnter: StorageDto) {
        this.enterStorageId = storageDtoEnter.id
        viewModelScope.launch {
            _enterStorageName.emit(storageDtoEnter.definition)
        }
    }

    fun setExitStorage(storageExitDto: StorageDto) {
        this.exitStorageId = storageExitDto.id
        viewModelScope.launch {
            _exitStorageName.emit(storageExitDto.definition)
        }
    }

    fun setEnteredProduct(dto: ProductDto) {
        productID = dto.id
        viewModelScope.launch {
            _productDetail.emit(dto)
            _showProductDetail.emit(true)
        }
    }
}