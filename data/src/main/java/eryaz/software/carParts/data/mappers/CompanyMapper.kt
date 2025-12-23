package eryaz.software.carParts.data.mappers

import eryaz.software.carParts.data.models.dto.CompanyDto
import eryaz.software.carParts.data.models.dto.StorageDto
import eryaz.software.carParts.data.models.dto.StorageTypeDto
import eryaz.software.carParts.data.models.dto.WarehouseDto
import eryaz.software.carParts.data.models.remote.response.CompanyResponse
import eryaz.software.carParts.data.models.remote.response.StorageResponse
import eryaz.software.carParts.data.models.remote.response.StorageTypeResponse
import eryaz.software.carParts.data.models.remote.response.WarehouseResponse

fun CompanyResponse.toDto() = CompanyDto(
    code = code,
    definition = definition,
    id = id
)

fun WarehouseResponse.toDto() = WarehouseDto(
    code = code,
    name = name,
    id = id
)

fun StorageTypeResponse.toDto()=StorageTypeDto(
    id = id,
    code = code,
    definition = definition
)

fun StorageResponse.toDto() = StorageDto(
    id =  id,
    code = code,
    definition = definition,
    warehouse = warehouse.toDto(),
    storageType = storageType?.toDto()
)