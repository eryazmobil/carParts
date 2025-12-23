package eryaz.software.carParts.data.models.dto

data class PickingSuggestionDto(

    val id: Int,
    val product: ProductDto,
    val shelfForPicking: StockShelfQuantityDto,
    val quantityWillBePicked: Int,
    var quantityPicked: Int,
    val controlPoint: ControlPointDto?,
    val collectPoint: String
)