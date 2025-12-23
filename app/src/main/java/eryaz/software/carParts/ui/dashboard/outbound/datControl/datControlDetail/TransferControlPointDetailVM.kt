package eryaz.software.carParts.ui.dashboard.outbound.datControl.datControlDetail

import androidx.lifecycle.viewModelScope
import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.BarcodeDto
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.PackageDto
import eryaz.software.carParts.data.models.dto.TransferRequestDetailDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransferControlPointDetailVM(
    private val workActivityRepo: WorkActivityRepo,
    val id: Int,
    val workActivityCode: String,
) : BaseViewModel() {

    private var productID: Int = 0
    private var transferRequestHeaderId: Int = 0

    val serialCheckBox = MutableStateFlow(false)
    var quantityCollected = MutableStateFlow("")
    var quantityShipped = MutableStateFlow("")
    var quantityOrder = MutableStateFlow("")
    val searchProduct = MutableStateFlow("")
    val enterQuantity = MutableStateFlow("")

    private val _transferDetailList = MutableStateFlow(listOf<TransferRequestDetailDto>())
    val transferDetailList = _transferDetailList.asStateFlow()

    private val _packageList = MutableStateFlow(listOf<PackageDto>())
    val packageList = _packageList.asStateFlow()

    private val _showSpinnerList = MutableStateFlow(false)
    val showSpinnerList = _showSpinnerList.asStateFlow()

    private val _productCode = MutableStateFlow("")
    val productCode = _productCode.asStateFlow()

    private val _productDetail = MutableStateFlow<BarcodeDto?>(null)
    val productDetail = _productDetail.asStateFlow()

    private val _showProductDetail = MutableStateFlow(false)
    val showProductDetail = _showProductDetail.asStateFlow()

    private val _scrollToPosition = MutableSharedFlow<Int>()
    val scrollToPosition = _scrollToPosition.asSharedFlow()

    private val _processSuccess = MutableSharedFlow<Boolean>()
    val processSuccess = _processSuccess.asSharedFlow()

    init {
        getOrderListDetail()
    }

    private fun getOrderListDetail() {
        executeInBackground(showProgressDialog = true) {
            workActivityRepo.getTransferDetailControlListForPda(workActivityId = id)
                .onSuccess {
                    if (it.transferRequestDetailPdaDto.isNotEmpty()) {
                        _transferDetailList.emit(it.transferRequestDetailPdaDto)
                        transferRequestHeaderId = it.transferRequestHeader.id
                        calculateDatQuantity()
                    } else {
                        showError(
                            ErrorDialogDto(
                                titleRes = R.string.error, messageRes = R.string.no_data_to_list
                            )
                        )
                    }
                }.onError { message, _ ->
                    showError(
                        ErrorDialogDto(
                            titleRes = R.string.error, message = message
                        )
                    )
                }
        }
    }

    fun getBarcodeByCode() {
        executeInBackground(
            showErrorDialog = false, showProgressDialog = true
        ) {
            workActivityRepo.getBarcodeByCode(
                code = searchProduct.value.trim(),
                companyId = SessionManager.companyId
            ).onSuccess {
                productID = it.product.id

                if (serialCheckBox.value) {
                    addQuantityForControl(1)
                } else {
                    _showProductDetail.emit(true)
                    _productDetail.emit(it)
                }

            }.onError { _, _ ->
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error, messageRes = R.string.msg_no_barcode
                    )
                )
            }
        }
    }

    fun addQuantityForControl(quantity: Int) {
        executeInBackground(showProgressDialog = true) {
            workActivityRepo.addQuantityForTransferRequestDetailControl(
                transferHeaderId = transferRequestHeaderId,
                productId = productID,
                quantity = quantity
            ).onSuccess {
                getOrderListDetail()
                transferDetailList.value.indexOfFirst { dto -> dto.product.id == productID }
                    .takeIf { index -> index >= 0 }?.apply {
                        _scrollToPosition.emit(this)
                    }
                _showProductDetail.emit(false)
                searchProduct.emit("")
                enterQuantity.emit("")
                _processSuccess.emit(true)
            }
        }
    }

    private fun calculateDatQuantity() {
        var sumQuantityCollected = 0
        var sumQuantityShipped = 0
        var sumQuantity = 0

        for (dto in _transferDetailList.value) {
            sumQuantityCollected += dto.quantityPicked.toInt()
            sumQuantityShipped += dto.quantityShipped.toInt()
            sumQuantity += dto.quantity.toInt()
        }

        viewModelScope.launch {
            quantityCollected.emit(sumQuantityCollected.toString())
            quantityShipped.emit(sumQuantityShipped.toString())
            quantityOrder.emit(sumQuantity.toString())
        }
    }

}