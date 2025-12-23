package eryaz.software.carParts.ui.dashboard.inbound.acceptance.acceptanceProcess

import androidx.lifecycle.viewModelScope
import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.ButtonDto
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.data.models.dto.WaybillListDetailDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AcceptanceProcessVM(
    private val repo: WorkActivityRepo
) : BaseViewModel() {

    private val SERIAL_WORK = 1

    val serialCheck = MutableStateFlow(false)
    private var waybillDetailList: List<WaybillListDetailDto> = emptyList()
    private var productID: Int = 0
    private var allowOverload: Boolean = false

    val searchProduct = MutableStateFlow("")
    val showCreateBarcode = MutableSharedFlow<Boolean>()
    val actionIsFinished = MutableStateFlow(false)
    val controlSuccess = MutableStateFlow<Boolean>(false)
    val quantity = MutableStateFlow("1")
    val multiplier = MutableStateFlow("")
    val qtyContainer = MutableStateFlow(0)
    val serialValue = MutableStateFlow("")

    private val _orderDate = MutableStateFlow("")
    val orderDate = _orderDate.asStateFlow()

    private val _clientName = MutableStateFlow("")
    val clientName = _clientName.asStateFlow()

    private val _productCode = MutableStateFlow("")
    val productCode = _productCode.asStateFlow()

    private val _showProductDetailView = MutableStateFlow(false)
    val showProductDetailView = _showProductDetailView.asStateFlow()

    private val _productDetail = MutableStateFlow<ProductDto?>(null)
    val productDetail = _productDetail.asStateFlow()

    private val _hasSerial = MutableStateFlow(false)
    val hasSerial = _hasSerial.asStateFlow()


    init {
        TemporaryCashManager.getInstance().workActivity?.let {

            viewModelScope.launch {
                _clientName.emit(it.client!!.name)
                _orderDate.emit(it.creationTime)
                _productCode.emit(it.workActivityCode)
            }
        }

        getWaybillListDetail()
    }

    private fun getWaybillListDetail() {
        executeInBackground(_uiState) {
            val workActivityID =
                TemporaryCashManager.getInstance().workActivity?.workActivityId ?: 0
            repo.getWaybillListDetail(
                workActivityId = workActivityID
            )
                .onSuccess {
                    waybillDetailList = it
                    checkIfAllFinished()
                }
        }
    }

    fun isQuantityValid(): Boolean {
        return quantity.value.isEmpty() || quantity.value == "0"
    }

    private fun isProductValid() {
        waybillDetailList.any {
            it.product.id == productID
        }.let { hasProduct ->
            if (hasProduct) {
                viewModelScope.launch {
                    if (!serialCheck.value)
                        _showProductDetailView.emit(true)
                }
            } else {
                viewModelScope.launch {
                    _showProductDetailView.emit(false)
                }
                showError(
                    ErrorDialogDto(
                        title = stringProvider.invoke(eryaz.software.carParts.data.R.string.error),
                        message = stringProvider.invoke(eryaz.software.carParts.data.R.string.no_exist_in_waybill)
                    )
                )
            }
        }
    }

    fun getBarcodeByCode() {
        executeInBackground(
            showErrorDialog = false,
            showProgressDialog = true
        ) {
            repo.getBarcodeByCode(
                searchProduct.value.trim(),
                SessionManager.companyId
            ).onSuccess {
                productID = it.product.id
                searchProduct.emit("")
                if (serialCheck.value) {
                    updateWaybillControlAddQuantity(SERIAL_WORK)
                } else {
                    _productDetail.emit(it.product)
                    _hasSerial.emit(it.product.hasSerial)
                    multiplier.emit("× " + it.quantity.toString())
                }
                isProductValid()
            }.onError { _, _ ->
                showCreateBarcode.emit(true)
                searchProduct.emit("")
            }
        }
    }

    private suspend fun checkIfAllFinished() {
        controlSuccess.emit(waybillDetailList.all { waybillDetail ->
            waybillDetail.quantityControlled.toInt() >= waybillDetail.quantity
        })
    }

    fun updateWaybillControlAddQuantity(quantity: Int) {
        TemporaryCashManager.getInstance().workAction?.let {
            executeInBackground(showErrorDialog = false, hasNextRequest = true) {
                repo.updateWaybillControlAddQuantity(
                    actionId = it.workActionId,
                    productId = productID,
                    quantity = quantity,
                    allowOverload = allowOverload,
                    serialLot = serialValue.value,
                    containerCount = qtyContainer.value
                ).onSuccess {
                    afterSuccessControl()
                    getWaybillListDetail()

                }.onError { message, _ ->
                    if (message?.startsWith("QTY-EXC") == true) {
                        val messageBody = stringProvider.invoke(R.string.msg_over_quantity_str) +
                                message.split(" ")[1] + stringProvider.invoke(R.string.msg_are_you_sure_for_this)
                        showError(
                            ErrorDialogDto(
                                title = stringProvider.invoke(R.string.msg_over_quantity),
                                message = messageBody,
                                positiveButton = ButtonDto(
                                    R.string.yes_uppercase,
                                    onClickListener = {
                                        allowOverload = true
                                        updateWaybillControlAddQuantity(quantity)
                                    }
                                )
                            )
                        )
                    } else {
                        showError(
                            ErrorDialogDto(
                                title = stringProvider.invoke(R.string.error),
                                message = message
                            )
                        )
                    }
                }
            }
        }
    }

    fun finishWorkAction() {
        TemporaryCashManager.getInstance().workAction?.let {
            executeInBackground {
                repo.finishWorkAction(
                    it.workActionId
                ).onSuccess {
                    actionIsFinished.emit(true)
                }.onError { message, _ ->
                    ErrorDialogDto(
                        title = stringProvider.invoke(R.string.error),
                        message = message
                    )
                }
            }
        }
    }

    private suspend fun afterSuccessControl() {
        _showProductDetailView.emit(false)
        this.quantity.emit("")
    }

    fun setEnteredProduct(dto: ProductDto) {
        productID = dto.id
        viewModelScope.launch {
            searchProduct.emit("")
            _productDetail.emit(dto)
            isProductValid()
        }
    }
}