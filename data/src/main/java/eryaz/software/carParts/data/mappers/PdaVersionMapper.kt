package eryaz.software.carParts.data.mappers

import eryaz.software.carParts.data.models.dto.PdaVersionDto
import eryaz.software.carParts.data.models.remote.models.PdaVersionModel

fun PdaVersionModel.toDto() = PdaVersionDto(
    version = version,
    downloadLink = downloadLink,
    id = id,
    apkZipName = downloadLink?.substringAfterLast("/"),
    apkFileName = downloadLink?.substringAfterLast("/")?.substringBeforeLast(".")
)
