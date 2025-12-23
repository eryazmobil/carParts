package eryaz.software.carParts.ui.dashboard.counting.firstCounting.firstCountingDetail

import androidx.lifecycle.viewModelScope
import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.data.models.dto.StockTackingProcessDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.repositories.CountingRepo
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import eryaz.software.carParts.util.extensions.orZero
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FirstCountingDetailVM(
    private val countingRepo: CountingRepo,
    private val workActivityRepo: WorkActivityRepo,
    val stHeaderId: Int
) : BaseViewModel() {

    var assignedShelfId: Int = 0
    private var productID: Int = 0
    private var stDetailId: Int = 0
    private var oldQuantity: Double = 0.0
    private var resultQuantity: Double = 0.0

    private val stockTakingActionProcessList =
        MutableStateFlow<List<StockTackingProcessDto>>(emptyList())

    val searchProduct = MutableStateFlow("")
    val searchShelf = MutableStateFlow("")
    var quantityEdt = MutableStateFlow("")

    val hasNotProductBarcode = MutableSharedFlow<Boolean>()
    val readShelfBarcode = MutableSharedFlow<Boolean>()
    val actionAddProduct = MutableSharedFlow<Boolean>()
    val actionProcess = MutableSharedFlow<Boolean>()
    val actionIsFinished = MutableSharedFlow<Boolean>()

    private val _productDetail = MutableStateFlow<ProductDto?>(null)
    val productDetail = _productDetail.asStateFlow()

    private val _showProductDetail = MutableStateFlow(false)
    val showProductDetail = _showProductDetail.asStateFlow()

    private val _showSaveBtn = MutableStateFlow(false)
    val showSaveBtn = _showSaveBtn.asStateFlow()

    fun getBarcodeByCode() {

        viewModelScope.launch {
            _showSaveBtn.emit(false)
        }

        executeInBackground(
            showErrorDialog = false,
            showProgressDialog = true
        ) {
            workActivityRepo.getBarcodeByCode(
                searchProduct.value.trim(), SessionManager.companyId
            ).onSuccess {
                if (it.product.id == 0) {
                    searchProduct.emit("")
                    showError(
                        ErrorDialogDto(
                            titleRes = R.string.error,
                            messageRes = R.string.msg_empty_shelf
                        )
                    )
                } else {
                    _productDetail.emit(it.product)
                    productID = it.product.id
                    _showProductDetail.emit(true)
                }
            }.onError { _, _ ->
                searchProduct.emit("")
                hasNotProductBarcode.emit(true)
            }
        }
    }

    fun getShelfByCode() {
        executeInBackground(
            showErrorDialog = true,
            showProgressDialog = true
        ) {
            workActivityRepo.getShelfByCode(
                code = searchShelf.value.trim(),
                warehouseId = SessionManager.warehouseId,
                storageId = 0
            ).onSuccess {
                assignedShelfId = it.shelfId
                getShelfIsOnAssignedUser()
                getSTActionProcessForShelf()

                _showSaveBtn.emit(true)
            }.onError { _, _ ->
                showError(
                    ErrorDialogDto(
                        title = stringProvider.invoke(R.string.error),
                        message = stringProvider.invoke(R.string.msg_shelf_not_found)
                    )
                )
                searchShelf.emit("")
            }
        }
    }

    fun getSTActionProcessListForShelf() {
        executeInBackground(showProgressDialog = true) {
            countingRepo.getSTActionProcessListForShelf(
                stHeaderId = stHeaderId,
                assignedShelfId = assignedShelfId
            ).onSuccess {
                if (it.isNotEmpty()) {
                    stockTakingActionProcessList.emit(it)
                    stDetailId = it[0].stockTakingDetail?.id.orZero()
                    actionProcess.emit(true)
                }
            }
        }
    }

    fun getSTActionProcessForShelf() {
        executeInBackground(showProgressDialog = true) {
            countingRepo.getSTActionProcessListForShelf(
                stHeaderId = stHeaderId,
                assignedShelfId = assignedShelfId
            ).onSuccess {
                if (it.isNotEmpty()) {
                    stockTakingActionProcessList.emit(it)
                    stDetailId = it[0].stockTakingDetail?.id.orZero()
                }
            }
        }
    }

    private fun getShelfIsOnAssignedUser() {
        executeInBackground(showProgressDialog = true) {
            countingRepo.getShelfIsOnAssignedUser(
                stHeaderId = stHeaderId,
                userId = SessionManager.userId,
                shelfId = assignedShelfId
            ).onSuccess {
                if (it) {
                    createSTActionProcessFromSTDetail()
                } else {
                    showError(
                        ErrorDialogDto(
                            titleRes = R.string.error,
                            messageRes = R.string.can_not_counting_in_shelf
                        )
                    )
                }
            }
        }
    }

    private fun createSTActionProcessFromSTDetail() {
        executeInBackground(showProgressDialog = true) {
            countingRepo.createSTActionProcessFromSTDetail(
                stHeaderId = stHeaderId,
                userId = SessionManager.userId,
                assignedShelfId = assignedShelfId
            ).onSuccess {
                readShelfBarcode.emit(true)
            }
        }
    }

    fun addProduct(addOn: Boolean) {
        viewModelScope.launch {
            _showSaveBtn.emit(true)
        }

        if (isValidFields()) {
            resultQuantity = if (addOn) {
                oldQuantity + quantityEdt.value.toDouble()
            } else {
                oldQuantity = 0.0
                quantityEdt.value.toDouble()
            }

            executeInBackground(showProgressDialog = true) {
                countingRepo.addProduct(
                    stHeaderId = stHeaderId,
                    userId = SessionManager.userId,
                    productId = productID,
                    assignedShelfId = assignedShelfId,
                    countedQuantity = resultQuantity.toInt()
                ).onSuccess {
                    actionAddProduct.emit(true)
                    searchProduct.value = ""
                    quantityEdt.value = ""
                    _showProductDetail.emit(false)
                    _productDetail.emit(null)
                }
            }
        }
    }

    fun checkProductHasControlled(): Boolean {
        val selectedProduct = stockTakingActionProcessList.value.find {
            it.productDto?.id == productID
        }

        oldQuantity = selectedProduct?.shelfCurrentQuantity ?: 0.0
        return selectedProduct != null
    }

    private fun isValidFields(): Boolean {
        when {
            assignedShelfId == 0 -> {
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error, messageRes = R.string.enter_shelf_
                    )
                )
                return false
            }

            productID == 0 -> {
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error, messageRes = R.string.product_code_empty
                    )
                )
                return false
            }

            quantityEdt.value.isEmpty() || quantityEdt.value.toInt() == 0 -> {
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error, messageRes = R.string.enter_valid_qty
                    )
                )
                return false
            }

            else -> return true
        }
    }

    fun setEnteredProduct(dto: ProductDto) {
        productID = dto.id
        viewModelScope.launch {
            searchProduct.emit("")
            _productDetail.emit(dto)
            _showProductDetail.emit(true)
        }
    }

    fun saveBtn() {
        executeInBackground(showProgressDialog = true) {
            countingRepo.finishSTDetail(
                stDetailId = stDetailId,
                userId = SessionManager.userId
            ).onSuccess {
                actionIsFinished.emit(true)
                searchShelf.value = ""
                searchProduct.value = ""
                quantityEdt.value = ""
                _showProductDetail.emit(false)
                _showSaveBtn.emit(false)
                _productDetail.emit(null)
            }
        }
    }

}