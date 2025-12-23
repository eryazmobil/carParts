package eryaz.software.carParts.data.mappers

import eryaz.software.carParts.data.models.dto.ClientDto
import eryaz.software.carParts.data.models.remote.response.ClientSmallResponse

fun ClientSmallResponse.toDto() = ClientDto(
    id = id,
    code = code,
    name = name ?: "Hatalı işlem"
)