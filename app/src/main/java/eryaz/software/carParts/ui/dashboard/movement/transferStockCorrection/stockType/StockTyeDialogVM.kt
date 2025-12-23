package eryaz.software.carParts.ui.dashboard.movement.transferStockCorrection.stockType

import eryaz.software.carParts.R
import eryaz.software.carParts.data.models.dto.StockTypeDto
import eryaz.software.carParts.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StockTyeDialogVM : BaseViewModel() {

    private val _stockTypeList = MutableStateFlow(getItemList())
    val stockTypeList = _stockTypeList.asStateFlow()

    private fun getItemList() =
            listOf(
                StockTypeDto(
                    type = 2,
                    titleRes = R.string.add_product_
                ),
                StockTypeDto(
                    type = 1,
                    titleRes = R.string.delete_product
                )
            )
}