package eryaz.software.carParts.data.mappers

import eryaz.software.carParts.data.models.dto.WaybillListDetailDto
import eryaz.software.carParts.data.models.remote.response.WaybillListDetailResponse

fun WaybillListDetailResponse.toDto() = WaybillListDetailDto(
    product = product.toDto(),
    quantity = quantity,
    quantityOrder = quantityOrder,
    quantityPlaced = quantityPlaced,
    quantityControlled = quantityControlled.toString(),
    placement = placement.orEmpty(),
    id = id
)