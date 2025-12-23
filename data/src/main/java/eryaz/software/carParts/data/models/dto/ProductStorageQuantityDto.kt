package eryaz.software.carParts.data.models.dto

data class ProductStorageQuantityDto(
    val id: Int,
    val product: ProductDto,
    val company: CompanyDto,
    val storage: StorageDto,
    val warehouse: WarehouseDto,
    val quantity: String,
    val storageText:String

)
