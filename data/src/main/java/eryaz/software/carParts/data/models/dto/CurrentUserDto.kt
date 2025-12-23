package eryaz.software.carParts.data.models.dto

data class CurrentUserDto(
    val userId:Int,
    val fullName: String,
    val email: String,
    val username: String,
    val companyId: Int,
    val warehouseId: Int,
)
