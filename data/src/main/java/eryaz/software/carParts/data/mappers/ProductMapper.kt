package eryaz.software.carParts.data.mappers

import eryaz.software.carParts.data.models.dto.ProductDto
import eryaz.software.carParts.data.models.dto.ProductShelfDto
import eryaz.software.carParts.data.models.dto.ProductShelfQuantityDto
import eryaz.software.carParts.data.models.dto.ProductStorageQuantityDto
import eryaz.software.carParts.data.models.dto.ShippingTypeDto
import eryaz.software.carParts.data.models.remote.response.ProductResponse
import eryaz.software.carParts.data.models.remote.response.ProductShelfQuantityResponse
import eryaz.software.carParts.data.models.remote.response.ProductShelfResponse
import eryaz.software.carParts.data.models.remote.response.ProductStorageQuantityResponse
import eryaz.software.carParts.data.models.remote.response.ShippingTypeResponse

fun ProductResponse.toDto() = ProductDto(
    company = company?.toDto(),
    code = code,
    definition = definition.orEmpty(),
    definition2 = definition2.orEmpty(),
    manufacturer = manufacturer.orEmpty(),
    manufacturerCode = manufacturerCode.orEmpty(),
    unit = unit.orEmpty(),
    hasSerial = hasSerial,
    id = id
)

fun ShippingTypeResponse.toDto() = ShippingTypeDto(
    code = code.orEmpty(),
    definition = definition.orEmpty(),
    id = id,
    shortCode = shortCode.orEmpty()
)

fun ProductShelfResponse.toDto() = ProductShelfDto(
    id = id,
    product = product.toDto(),
    shelf = shelf.toDto(),
    safetyPercent = safetyPercent,
    maxQuantity = maxQuantity,
    quantity = quantity
)

fun ProductStorageQuantityResponse.toDto() = ProductStorageQuantityDto(
    id = id,
    product = product.toDto(),
    warehouse = warehouse.toDto(),
    storage = storage.toDto(),
    quantity = quantity.toString(),
    company = company.toDto(),
    storageText = "${storage.code} - ${storage.definition}"
)

fun ProductShelfQuantityResponse.toDto() = ProductShelfQuantityDto(
    id = id,
    product = product.toDto(),
    warehouse = warehouse.toDto(),
    storage = storage.toDto(),
    quantity = quantity.toInt().toString(),
    company = company.toDto(),
    storageText = "${storage.code} - ${storage.definition}",
    shelf = shelf?.toDto()
)