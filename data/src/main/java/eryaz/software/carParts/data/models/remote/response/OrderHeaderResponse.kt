package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class OrderHeaderResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("company")
    val company: CompanyResponse?,
    @SerializedName("client")
    val client: ClientSmallResponse?,
    @SerializedName("warehouse")
    val warehouse: WarehouseResponse?,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("shippingType")
    val shippingType: ShippingTypeResponse?,
    @SerializedName("controlPoint")
    val controlPoint: ControlPointResponse?,
    @SerializedName("collectPoint")
    val collectPoint: String?,
    @SerializedName("documentNo")
    val documentNo: String,
    @SerializedName("documentDate")
    val documentDate: String,
    @SerializedName("workActivity")
    val workActivity: WorkActivityResponse?
)