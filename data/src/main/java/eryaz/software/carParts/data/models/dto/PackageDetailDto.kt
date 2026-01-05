package eryaz.software.carParts.data.models.dto

data class PackageDetailDto(
    val packageId: Int,
    val packageCode: String,
    val orderNumber: String,
    val clientCode: String,
    val controlPointCode: String,
    val quantity: Int,
)
