package eryaz.software.carParts.data.mappers

import eryaz.software.carParts.data.models.dto.BarcodeDto
import eryaz.software.carParts.data.models.remote.response.BarcodeResponse

fun BarcodeResponse.toDto() = BarcodeDto(
    id = id,
    product = product.toDto(),
    code = code,
    quantity = quantity
)