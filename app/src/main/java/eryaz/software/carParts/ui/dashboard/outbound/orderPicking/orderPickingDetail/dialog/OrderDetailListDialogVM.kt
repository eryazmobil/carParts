package eryaz.software.carParts.ui.dashboard.outbound.orderPicking.orderPickingDetail.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import eryaz.software.carParts.R
import eryaz.software.carParts.data.api.utils.onError
import eryaz.software.carParts.data.api.utils.onSuccess
import eryaz.software.carParts.data.models.dto.ErrorDialogDto
import eryaz.software.carParts.data.models.dto.OrderDetailDto
import eryaz.software.carParts.data.persistence.TemporaryCashManager
import eryaz.software.carParts.data.repositories.OrderRepo
import eryaz.software.carParts.ui.base.BaseViewModel
import eryaz.software.carParts.util.extensions.orZero

class OrderDetailListDialogVM(
    private val repo: OrderRepo
) : BaseViewModel() {

    private val _orderDetailList = MutableLiveData<List<OrderDetailDto>>(emptyList())
    val orderDetailList: LiveData<List<OrderDetailDto>> = _orderDetailList

    val search = MutableLiveData("")

    init {
        getOrderListDetail()
    }

    fun searchList() = search.switchMap { query ->
        MutableLiveData<List<OrderDetailDto>>().apply {
            value = _orderDetailList.value.orEmpty().filter { data ->
                data.product.code.contains(query, ignoreCase = true)
            }
        }
    }

    fun getOrderListDetail() {
        executeInBackground(showProgressDialog = true) {
            val workActivityId =
                TemporaryCashManager.getInstance().workActivity?.workActivityId.orZero()
            repo.getOrderDetailListForPda(workActivityId = workActivityId)
                .onSuccess {
                    _orderDetailList.value = it
                }.onError { message, _ ->
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