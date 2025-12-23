package eryaz.software.carParts.ui.dashboard.inbound.acceptance.acceptanceProcess.createBarcode

import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.data.models.remote.request.BarcodeRequestModel
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.data.repositories.BarcodeRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import eryaz.software.carParts.util.CombinedStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateBarcodeDialogVM(private val repo: BarcodeRepo) : BaseViewModel() {

    val productCode = MutableStateFlow("")
    val productBarcode = MutableStateFlow("")
    private var productId = 0
    val productQuantity = MutableStateFlow("")
    private var _productDetail: ProductDto? = null

    private val _productDetailCode = MutableStateFlow("")
    val productDetailCode = _productDetailCode.asStateFlow()

    private val _productDetailDefinition = MutableStateFlow("")
    val productDetailDefinition = _productDetailDefinition.asStateFlow()

    private val _productDetailManufacturer = MutableStateFlow("")
    val productDetailManufacturer = _productDetailManufacturer.asStateFlow()

    private val _showProductViewDetail = MutableStateFlow(false)
    val showProductViewDetail = _showProductViewDetail.asStateFlow()

    private val _showSuccess = MutableSharedFlow<Boolean>()
    val showSuccess = _showSuccess.asSharedFlow()

    private val _showErrorProduct = MutableSharedFlow<Boolean>()
    val showErrorProduct = _showErrorProduct.asSharedFlow()

    private val _showErrorProductMessage = MutableSharedFlow<String>()
    val showErrorProductMessage = _showErrorProductMessage.asSharedFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    val saveEnabled = CombinedStateFlow(productCode, productBarcode, productQuantity) {
        productBarcode.value.isNotEmpty() && productQuantity.value.isNotEmpty()
    }

    fun getProductByCode() {
        executeInBackground(showErrorDialog = false) {
            val companyId = SessionManager.companyId
            repo.getProductByCode(
                companyId = companyId,
                code = productCode.value
            ).onSuccess {
                _productDetail = it
                productId = it.id

                _productDetailCode.emit(it.code)
                _productDetailDefinition.emit(it.definition)
                _productDetailManufacturer.emit(it.manufacturer)
                _showProductViewDetail.emit(true)
            }
                .onError { _, _ ->
                    _showErrorProduct.emit(true)
                }
        }
    }

    fun createBarcode() {
        val createBarcode = BarcodeRequestModel(
            productId = productId,
            companyId = SessionManager.companyId,
            code = productBarcode.value,
            quantity = productQuantity.value.toInt()
        )
        executeInBackground(showErrorDialog = false) {
            repo.createBarcode(
                createBarcode
            ).onSuccess {
                _showSuccess.emit(true)
            }.onError { message, _ ->
                _showErrorProductMessage.emit(message.toString())
            }
        }
    }

}