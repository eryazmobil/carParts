package eryaz.software.carParts.data.models.dto


data class ControlPointScreenDto(
    val id: Int,
    val code: String,
    val definition: String,
    val status: String,
    val pickerUsers: String,
    val shippingTypes: String,
    val clientNames: String,
    val fixName:String,
)
