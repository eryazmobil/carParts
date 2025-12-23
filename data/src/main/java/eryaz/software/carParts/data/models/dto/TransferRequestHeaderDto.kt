package eryaz.software.carParts.data.models.dto

data class TransferRequestHeaderDto(
    val id: Int,
    val note: String,
    val shippingType: String
)