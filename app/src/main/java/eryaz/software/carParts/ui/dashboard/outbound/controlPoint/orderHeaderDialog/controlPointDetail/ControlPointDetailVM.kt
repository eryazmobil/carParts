package eryaz.software.carParts.ui.dashboard.outbound.controlPoint.orderHeaderDialog.controlPointDetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.OrderDetailDto
import eryaz.software.carParts.data.models.dto.PackageDto
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.data.models.dto.WorkActivityDto
import eryaz.software.carParts.data.persistence.SessionManager
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
import kotlin.collections.filter
import kotlin.collections.orEmpty

class ControlPointDetailVM(
    private val orderRepo: OrderRepo,
    private val workActivityRepo: WorkActivityRepo,
    val workActivityCode: String,
    val orderHeaderId: Int
) : BaseViewModel() {

    private var packageListPosition: Int = 0
    private var productID: Int = 0
    private var isPackage: Boolean = false
    var selectedPackageId: Int = 0
    var selectedPackageDto: PackageDto? = null

    val search = MutableLiveData("")

    val serialCheckBox = MutableStateFlow(false)
    val controlSuccess = MutableSharedFlow<Boolean>()
    var quantityCollected = MutableStateFlow("")
    var quantityShipped = MutableStateFlow("")
    var quantityOrder = MutableStateFlow("")
    val searchProduct = MutableStateFlow("")
    val quantity = MutableStateFlow("")

    private val _orderDetailList = MutableStateFlow(listOf<OrderDetailDto>())
    val orderDetailList = _orderDetailList.asStateFlow()

    private val _packageList = MutableStateFlow(listOf<PackageDto>())
    val packageList = _packageList.asStateFlow()

    private val _showSpinnerList = MutableStateFlow(false)
    val showSpinnerList = _showSpinnerList.asStateFlow()

    private val _productCode = MutableStateFlow("")
    val productCode = _productCode.asStateFlow()

    private val _willControlled = MutableStateFlow("")
    val willControlled = _willControlled.asStateFlow()

    private val _productDetail = MutableStateFlow<ProductDto?>(null)
    val productDetail = _productDetail.asStateFlow()

    private val _showProductDetail = MutableStateFlow(false)
    val showProductDetail = _showProductDetail.asStateFlow()

    private val _showProductControl = MutableStateFlow(false)
    val showProductControl = _showProductControl.asStateFlow()

    private val _scrollToPosition = MutableSharedFlow<Int>()
    val scrollToPosition = _scrollToPosition.asSharedFlow()

    val controlEnableBtn = CombinedStateFlow(quantity) {
        quantity.value.isNotEmpty()
    }

    init {
        getOrderListDetail()
        getPackageList()
    }

    fun searchList() = search.switchMap { query ->
        MutableLiveData<List<OrderDetailDto?>>().apply {
            value = filterData(query)
        }
    }

    private fun filterData(query: String): List<OrderDetailDto?> {
        val dataList = _orderDetailList.value

        val filteredList = dataList.filter { data ->
            data.product.code.contains(query, ignoreCase = true)
        }
        return filteredList
    }

    private fun getOrderListDetail() {
        executeInBackground(showProgressDialog = true) {
            orderRepo.getOrderDetailList(headerId = orderHeaderId).onSuccess {
                if (it.isNotEmpty()) {
                    _orderDetailList.emit(it)
                    controlSuccess.emit(true)

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
                code = searchProduct.value.trim(), companyId = SessionManager.companyId
            ).onSuccess {
                productID = it.product.id
                findProductForControl()
                orderDetailList.value.indexOfFirst { dto -> dto.product.id == productID }
                    .takeIf { index -> index >= 0 }?.apply {
                        _scrollToPosition.emit(this)
                    }

                if (serialCheckBox.value) {
                    addQuantityForControl(1)
                } else {
                    _showProductDetail.emit(true)
                    _showProductControl.emit(true)
                    _productDetail.emit(it.product)
                }

            }.onError { _, _ ->
                showError(
                    ErrorDialogDto(
                        titleRes = R.string.error, messageRes = R.string.msg_no_barcode
                    )
                )
            }.apply {
                searchProduct.emit("")
            }
        }
    }

    fun addQuantityForControl(quantityInt: Int) {
        executeInBackground(showProgressDialog = true, hasNextRequest = true) {
            orderRepo.addQuantityForControl(
                orderHeaderId = orderHeaderId,
                productId = productID,
                quantity = quantityInt,
                isControlDoubleClick = false,
                isPackage = isPackage,
                packageId = selectedPackageId
            ).onSuccess {
                searchProduct.emit("")
                quantity.emit("")

                _showProductDetail.emit(false)
                _showProductControl.emit(false)
                if (it.isNotEmpty()) {
                    getOrderListDetail()
                }


            }
        }
    }

    fun fake() {
        viewModelScope.launch {
            searchProduct.emit("")
            quantity.emit("")

            _showProductDetail.emit(false)
            _showProductControl.emit(false)
            getOrderListDetail()
        }

    }

    fun getPackageList() {
        executeInBackground {
            orderRepo.getPackageList(orderHeaderId = orderHeaderId).onSuccess {
                if (it.isNotEmpty()) {
                    _showSpinnerList.emit(true)
                    val itemList = mutableListOf(
                        PackageDto(
                            no = stringProvider.invoke(R.string.choose_package)
                        )
                    )
                    itemList.addAll(it)
                    _packageList.emit(itemList)
                }
            }
        }
    }

    fun setSelectedPackagePosition(position: Int) {
        this.packageListPosition = position
        isPackage = true
        selectedPackageId = _packageList.value[position].id.orZero()
        selectedPackageDto = _packageList.value[position]
    }

    fun getSelectedPackagePosition(): Int {
        return this.packageListPosition
    }

    private fun calculateDatQuantity() {
        var sumQuantityCollected = 0
        var sumQuantityShipped = 0
        var sumQuantity = 0

        for (dto in _orderDetailList.value) {
            sumQuantityCollected += dto.quantityCollected.toInt()
            sumQuantityShipped += dto.quantityShipped.toInt()
            sumQuantity += dto.quantity.toInt()
        }

        viewModelScope.launch {
            quantityCollected.emit(sumQuantityCollected.toString())
            quantityShipped.emit(sumQuantityShipped.toString())
            quantityOrder.emit(sumQuantity.toString())
        }
    }

    fun setEnteredProduct(dto: ProductDto) {
        productID = dto.id

        findProductForControl()
        viewModelScope.launch {
            _showProductDetail.emit(true)
            _showProductControl.emit(true)
            searchProduct.emit("")
            _productDetail.emit(dto)
        }
    }

    fun findProductForControl() {
        viewModelScope.launch {
            val remainingToCollect = orderDetailList.value
                .filter { orderDetail ->
                    orderDetail.product.id == productID
                }
                .sumOf { orderDetail ->
                    val shipped = orderDetail.quantityShipped.toDoubleOrNull() ?: 0.0
                    val collected = orderDetail.quantityCollected.toDoubleOrNull() ?: 0.0
                    collected - shipped
                }

            _willControlled.emit(remainingToCollect.toString())
        }
    }

}