package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class ProductStorageQuantityResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("product")
    val product: ProductResponse,

    @SerializedName("company")
    val company: CompanyResponse,

    @SerializedName("warehouse")
    val warehouse: WarehouseResponse,

    @SerializedName("storage")
    val storage: StorageResponse,

    @SerializedName("quantity")
    val quantity: Int
)