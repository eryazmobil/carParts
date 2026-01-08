package eryaz.software.carParts.ui.dashboard.outbound.orderPicking.orderPickingDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.ButtonDto
import eryaz.software.carParts.data.models.dto.ConfirmationDialogDto
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.OrderDetailDto
import eryaz.software.carParts.data.models.dto.OrderPickingDto
import eryaz.software.carParts.data.models.dto.PickingSuggestionDto
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.data.models.dto.ProductShelfQuantityDto
import eryaz.software.carParts.data.models.dto.WorkActionDto
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.data.repositories.OrderRepo
import eryaz.software.carParts.data.repositories.WorkActivityRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import eryaz.software.carParts.util.CombinedStateFlow
import eryaz.software.carParts.util.extensions.orZero
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderPickingDetailVM(
    private val orderRepo: OrderRepo,
    private val workActivityRepo: WorkActivityRepo
) : BaseViewModel() {

    val productBarcode = MutableStateFlow("")
    val enteredQuantity = MutableStateFlow("")
    val shelfAddress = MutableStateFlow("")
    private val fifoCode = MutableStateFlow(" ")
    val parentView = MutableStateFlow(false)

    val stockNotEnough = MutableSharedFlow<Boolean>()
    val productRequestFocus = MutableSharedFlow<Boolean>()

    var productId: Int = 0

    var orderDetailList: List<OrderDetailDto> = emptyList()

    private var orderPickingDto: OrderPickingDto? = null
    private var selectedOrderDetailProduct: OrderDetailDto? = null

    private var selectedSuggestionIndex: Int = -1
    private var shelfId: Int = 0

    private val _selectedSuggestion = MutableStateFlow<PickingSuggestionDto?>(null)
    var selectedSuggestion = _selectedSuggestion.asStateFlow()

    private val _showProductDetail = MutableStateFlow(false)
    val showProductDetail = _showProductDetail.asStateFlow()

    private val _pickedAndOrderQty = MutableStateFlow("")
    val pickedAndOrderQty = _pickedAndOrderQty.asStateFlow()

    private val _orderQuantityTxt = MutableStateFlow("")
    val orderQuantityTxt = _orderQuantityTxt.asStateFlow()

    private val _controlQtyAndCollectPoint = MutableStateFlow("")
    val controlQtyAndCollectPoint = _controlQtyAndCollectPoint.asStateFlow()

    private val _productQuantity = MutableStateFlow("")
    val productQuantity = _productQuantity.asStateFlow()

    private val _productDetail = MutableStateFlow<ProductDto?>(null)
    val productDetail = _productDetail.asStateFlow()

    private val _createStockOut = MutableStateFlow(false)
    val createStockOut = _createStockOut.asStateFlow()

    private val _shelfRead = MutableSharedFlow<Boolean>()
    val shelfRead = _shelfRead.asSharedFlow()

    private val _nextOrder = MutableStateFlow(false)
    val nextOrder = _nextOrder.asStateFlow()

    private val _finishWorkAction = MutableStateFlow(false)
    val finishWorkAction = _finishWorkAction.asStateFlow()

    private val _orderPickingList = MutableLiveData<List<PickingSuggestionDto?>>(emptyList())
    val orderPickingList: LiveData<List<PickingSuggestionDto?>> = _orderPickingList

    private val _workActionDto = MutableSharedFlow<WorkActionDto>()
    val workActionDto = _workActionDto.asSharedFlow()

    private val _pageNum = MutableStateFlow("")
    val pageNum = _pageNum.asStateFlow()

    private val _shelfList = MutableStateFlow(listOf<ProductShelfQuantityDto>())
    val shelfList = _shelfList.asStateFlow()

    val pickEnableBtn = CombinedStateFlow(enteredQuantity) {
        enteredQuantity.value.isNotEmpty()
    }

    init {
        getOrderDetailPickingList()
    }

    fun getOrderDetailPickingList(forRefresh: Boolean = false) = executeInBackground(_uiState) {
        orderRepo.getOrderDetailPickingList(
            workActivityId = TemporaryCashManager.getInstance().workActivity?.workActivityId.orZero(),
            userId = SessionManager.userId
        ).onSuccess {
            if (it.orderDetailList.isNotEmpty()) {

                orderDetailList = it.orderDetailList

                if (it.pickingSuggestionList.isNotEmpty()) {
                    orderPickingDto = it

                    if (!forRefresh)
                        showNext()
                } else {
                    checkAllOrderCompleted(it.orderDetailList)
                }

            } else {
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error, messageRes = R.string.work_activity_error_2
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

    private suspend fun checkAllOrderCompleted(orderList: List<OrderDetailDto>) {
        val allIsCollected = orderList.all { it.quantityCollected == it.quantity }
        if (allIsCollected) {
            parentView.emit(true)
        } else {
            stockNotEnough.emit(true)
        }

    }

    private fun createStockOut() {
        executeInBackground(showProgressDialog = true) {
            orderRepo.createStockOut(
                productId = selectedSuggestion.value?.product?.id.orZero(), shelfId = shelfId
            ).onSuccess {
                _createStockOut.emit(it)
            }
        }
    }

    fun getBarcodeByCode() {
        executeInBackground(showProgressDialog = true) {
            workActivityRepo.getBarcodeByCode(
                code = productBarcode.value, companyId = SessionManager.companyId
            ).onSuccess {
                productId = it.product.id
                _productQuantity.emit("x " + it.quantity.toString())
                _productDetail.emit(it.product)

                checkProductOrder()
            }.onError { _, _ ->
                productBarcode.emit("")
                _showProductDetail.emit(false)

                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error, messageRes = R.string.msg_no_barcode
                    )
                )
            }
        }
    }

    fun getShelfByCode() {
        executeInBackground(
            showErrorDialog = true, showProgressDialog = true, hasNextRequest = true
        ) {
            workActivityRepo.getShelfByCode(
                code = shelfAddress.value.trim(),
                warehouseId = SessionManager.warehouseId,
                storageId = 0
            ).onSuccess {
                shelfId = it.shelfId

                getProductShelfQuantityList()
            }.onError { _, _ ->
                shelfAddress.emit("")

                showError(
                    ErrorDialogDto(
                        title = stringProvider.invoke(eryaz.software.carParts.data.R.string.error),
                        message = stringProvider.invoke(eryaz.software.carParts.data.R.string.msg_shelf_not_found)
                    )
                )
            }
        }
    }

    private fun getProductShelfQuantityList() = executeInBackground(
        showErrorDialog = true, showProgressDialog = true
    ) {
        workActivityRepo.getProductShelfQuantityList(
            productId = productId,
            shelfId = shelfId,
            warehouseId = SessionManager.warehouseId,
            companyId = SessionManager.companyId,
            storageId = 0
        ).onSuccess {
            _shelfList.emit(it)
            checkProductInShelf()
        }
    }

    fun updateOrderDetailCollectedAddQuantityForPda() {
        executeInBackground(showProgressDialog = true) {
            val quantity = enteredQuantity.value.toInt()

            orderRepo.updateOrderDetailCollectedAddQuantityForPda(
                workActionId = TemporaryCashManager.getInstance().workAction?.workActionId.orZero(),
                productId = productId,
                shelfId = shelfId,
                containerId = 0,
                quantity = quantity,
                orderDetailId = selectedOrderDetailProduct?.id.orZero(),
                fifoCode = fifoCode.value
            ).onSuccess {
                updateOrderQuantity()
                checkPickingFromOrder()

                enteredQuantity.value = ""
                shelfAddress.value = ""


                _showProductDetail.emit(false)
                productRequestFocus.emit(true)
            }
        }
    }

    fun checkCrossDockNeedByActionId() {
        executeInBackground {
            orderRepo.checkCrossDockNeedByActionId(
                workActionId = TemporaryCashManager.getInstance().workAction?.workActionId.orZero()
            ).onSuccess {
                if (it) {
                    showConfirmation(
                        ConfirmationDialogDto(
                            titleRes = R.string.attention,
                            messageRes = R.string.picking_not_completed,
                            positiveButton = ButtonDto(
                                text = R.string.create_crossdock,
                                onClickListener = {
                                    showConfirmation(
                                        ConfirmationDialogDto(
                                            titleRes = R.string.attention,
                                            messageRes = R.string.are_you_sure,
                                            positiveButton = ButtonDto(
                                                text = R.string.yes,
                                                onClickListener = {
                                                    //krosdock olustur
                                                    createCrossDockRequest()
                                                }
                                            ), negativeButton = ButtonDto(
                                                text = R.string.no
                                            )
                                        )
                                    )
                                }
                            ),
                            negativeButton = ButtonDto(
                                text = R.string.i_leave_half,
                                onClickListener = {
                                    finishWorkAction()
                                }
                            )
                        )
                    )
                } else {
                    finishWorkAction()
                }
            }
        }
    }

    private fun createCrossDockRequest() {
        executeInBackground(showErrorDialog = true, showProgressDialog = true) {
            orderRepo.createCrossDockRequest(
                workActionId = TemporaryCashManager.getInstance().workAction?.workActionId.orZero()
            ).onSuccess {
                finishWorkAction()
            }
        }
    }

    fun finishWorkAction() {
        executeInBackground(showProgressDialog = true) {
            workActivityRepo.finishWorkAction(actionId = TemporaryCashManager.getInstance().workAction?.workActionId.orZero())
                .onSuccess {
                    _finishWorkAction.emit(true)
                }
        }
    }

    private fun updateOrderQuantity() {
        var quantity = enteredQuantity.value.toInt()

        selectedOrderDetailProduct?.let {
            if (quantity > 0) {
                var collected = it.quantityCollected.toInt()
                collected += quantity
            }
        }

        if (quantity > 0) {
            if (selectedSuggestion.value?.quantityWillBePicked?.minus(selectedSuggestion.value?.quantityPicked.orZero())
                    .orZero() >= quantity
            ) {
                selectedSuggestion.value?.let {
                    it.quantityPicked += quantity
                }
                quantity = 0
            } else {
                val otherQuantity =
                    selectedSuggestion.value?.quantityWillBePicked?.minus(selectedSuggestion.value?.quantityPicked.orZero())
                selectedSuggestion.value?.let {
                    it.quantityPicked += otherQuantity.orZero()
                }

                quantity -= otherQuantity.orZero()
            }
        }

        selectedSuggestion.value?.let { dto ->
            if (dto.quantityWillBePicked.orZero() == dto.quantityPicked.orZero()) {
                orderPickingDto?.pickingSuggestionList?.toMutableList()?.apply {
                    indexOfFirst {
                        it.quantityWillBePicked.orZero() == it.quantityPicked.orZero()
                    }.let { index ->

                        val deletedOrderDetailListIx =
                            orderPickingDto?.orderDetailList?.indexOfFirst {
                                it.id == orderPickingDto?.pickingSuggestionList?.get(index)?.id
                            } ?: -1

                        if (index != -1 && deletedOrderDetailListIx != -1) {
                            removeAt(index)

                            orderPickingDto?.orderDetailList?.toMutableList()?.apply {
                                removeAt(deletedOrderDetailListIx)
                                orderPickingDto?.orderDetailList = this
                            }

                            orderPickingDto?.pickingSuggestionList = this
                            selectedSuggestionIndex = index - 1

                            showNext()
                        }
                    }

                    if (isEmpty()) {
                        viewModelScope.launch {
                            parentView.emit(true)
                        }
                    }
                }
            }
        }
    }

    private fun checkPickingFromOrder() {
        val isQuantityCollectedLess = orderPickingDto?.orderDetailList?.any {

            it.quantityCollected.toInt() < it.quantity.toInt()
        } ?: false

        if (!isQuantityCollectedLess) {
            finishWorkAction()
        }
    }

    private fun checkProductOrder() {
        orderPickingDto?.orderDetailList?.find {
            it.quantityCollected.toInt() < it.quantity.toInt() && it.product.id == productId
        }?.let { orderDetail ->
            selectedOrderDetailProduct = orderDetail

            viewModelScope.launch {
                productBarcode.emit("")
                _showProductDetail.emit(true)
                _controlQtyAndCollectPoint.emit("${orderDetail.orderHeader?.controlPoint?.code} / ${orderDetail.orderHeader?.collectPoint}")

                orderPickingDto?.pickingSuggestionList?.indexOfFirst { it.product.id == orderDetail.product.id }
                    ?.let { index ->
                        if (index != -1) {
                            selectedSuggestionIndex = index - 1
                            _pickedAndOrderQty.emit(
                                "${orderPickingDto?.pickingSuggestionList?.get(index)?.quantityPicked} / ${
                                    orderPickingDto?.pickingSuggestionList?.get(
                                        index
                                    )?.quantityWillBePicked
                                }"
                            )
                        }
                        showNext()
                    }
            }
        } ?: showError(
            ErrorDialogDto(
                title = stringProvider.invoke(eryaz.software.carParts.data.R.string.error),
                message = stringProvider.invoke(R.string.msg_not_in_picking_list)
            )
        )
    }

    private fun checkProductInShelf() {
        if (_shelfList.value.none {
                it.quantity.isNotEmpty()
            }) {
            showError(
                ErrorDialogDto(
                    titleRes = R.string.warning, messageRes = R.string.msg_not_in_this_shelf
                )
            )
        } else {
            viewModelScope.launch {
                _shelfRead.emit(true)
            }
        }
    }

    fun showNext() {
        viewModelScope.launch {
            selectedSuggestionIndex++

            orderPickingDto?.pickingSuggestionList?.getOrNull(selectedSuggestionIndex)?.let {
                _selectedSuggestion.emit(it)

                productId = it.product.id
            } ?: run {
                selectedSuggestionIndex--
                orderPickingDto?.pickingSuggestionList?.getOrNull(selectedSuggestionIndex)?.let {
                    _selectedSuggestion.emit(it)

                    productId = it.product.id
                }
            }

            _orderQuantityTxt.emit(
                "${orderPickingDto?.pickingSuggestionList?.getOrNull(selectedSuggestionIndex)?.quantityPicked} / " +
                        "${orderPickingDto?.pickingSuggestionList?.getOrNull(selectedSuggestionIndex)?.quantityWillBePicked}"
            )

            _pageNum.emit("${selectedSuggestionIndex + 1} / ${orderPickingDto?.pickingSuggestionList?.size}")
        }
    }

    fun showPrevious() {
        viewModelScope.launch {
            selectedSuggestionIndex--

            orderPickingDto?.pickingSuggestionList?.getOrNull(selectedSuggestionIndex)?.let {
                _selectedSuggestion.emit(it)
                productId = it.product.id
            } ?: run { selectedSuggestionIndex++ }

            _pageNum.emit("${selectedSuggestionIndex + 1} / ${orderPickingDto?.pickingSuggestionList?.size}")

            _orderQuantityTxt.emit(
                "${orderPickingDto?.pickingSuggestionList?.getOrNull(selectedSuggestionIndex)?.quantityPicked} / " +
                        "${orderPickingDto?.pickingSuggestionList?.getOrNull(selectedSuggestionIndex)?.quantityWillBePicked}"
            )
        }
    }

    fun showInfo() {
        showError(
            ErrorDialogDto(
                titleRes = R.string.attention,
                message = "${selectedSuggestion.value?.product?.code} ${stringProvider.invoke(R.string.stock_out_message_1)}" + "${selectedSuggestion.value?.shelfForPicking?.shelf?.shelfAddress} ${
                    stringProvider.invoke(
                        R.string.stock_out_message_2
                    )
                }",
                positiveButton = ButtonDto(text = R.string.confirm, onClickListener = {
                    createStockOut()
                }),
                negativeButton = ButtonDto(
                    text = R.string.cancel
                )
            )
        )
    }

    fun setEnteredProduct(dto: ProductDto) {
        productId = dto.id

        viewModelScope.launch {
            _productDetail.emit(dto)
            _productQuantity.emit("x " + 1)
            _showProductDetail.emit(false)
            checkProductOrder()
        }
    }
}