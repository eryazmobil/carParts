package eryaz.software.carParts.data.models.dto

data class OrderPickingDto(

    var orderDetailList: List<OrderDetailDto>,
    var pickingSuggestionList: List<PickingSuggestionDto>,
)