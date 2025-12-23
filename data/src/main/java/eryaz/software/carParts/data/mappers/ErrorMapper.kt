package eryaz.software.carParts.data.mappers

import eryaz.software.carParts.data.models.dto.ErrorDto
import eryaz.software.carParts.data.models.remote.response.ErrorModel

fun ErrorModel.toDto() = ErrorDto(
    code = code,
    message = message
)