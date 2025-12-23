package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class PackageResponse(
    @SerializedName("company")
    val company: CompanyResponse,
    @SerializedName("warehouse")
    val warehouse: WarehouseResponse,
    @SerializedName("orderHeader")
    val orderHeader: OrderHeaderResponse,
    @SerializedName("client")
    val client: ClientSmallResponse,
    @SerializedName("isLocked")
    val isLocked: Boolean,
    @SerializedName("no")
    val no: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("tareWeight")
    val tareWeight: Double,
    @SerializedName("sizeWidth")
    val sizeWidth: Double,
    @SerializedName("sizeLength")
    val sizeLength: Double,
    @SerializedName("sizeHeight")
    val sizeHeight: Double,
    @SerializedName("deci")
    val deci: Double,
    )