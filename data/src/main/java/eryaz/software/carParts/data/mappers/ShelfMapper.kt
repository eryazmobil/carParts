package eryaz.software.carParts.data.mappers

import eryaz.software.carParts.data.models.dto.ProductShelfQuantityDto
import eryaz.software.carParts.data.models.dto.ProductSpecialShelfDto
import eryaz.software.carParts.data.models.dto.ShelfDto
import eryaz.software.carParts.data.models.remote.models.ShelfStorageModel
import eryaz.software.carParts.data.models.remote.response.ProductSpecialShelfResponse
import eryaz.software.carParts.data.models.remote.response.ShelfResponse

fun ShelfResponse.toDto() = ShelfDto(
    shelfAddress = fullAddress,
    shelfId = shelfId,
    quantity = quantity.toInt().toString()
)

fun ProductSpecialShelfResponse.toDto() = ProductSpecialShelfDto(
    shelfDto = shelf?.toDto(),
    product = product.toDto(),
    quantity = quantity.toString()
)

fun ProductShelfQuantityDto.toShelfStorageModel() = ShelfStorageModel(
    shelfDto = shelf,
    storageDto = storage,
    quantity = quantity
)