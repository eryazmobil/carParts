package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PackageDto(
    val company: CompanyDto?=null,
    val warehouse: WarehouseDto?=null,
    val orderHeader: OrderHeaderDto?=null,
    val client: ClientDto?=null,
    val isLocked: Boolean = false,
    val no: String,
    val id: Int?=null,
    val tareWeight: Double?=null,
    val sizeWidth: Double?=null,
    val sizeLength: Double?=null,
    val sizeHeight: Double?=null,
    val deci: Double?=null
):Parcelable{

    override fun toString(): String {
        return no
    }
}