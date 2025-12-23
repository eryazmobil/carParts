package eryaz.software.carParts.data.models.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderHeaderDto(
    val id: Int,
    val company: CompanyDto,
    val client: ClientDto?,
    val warehouse: WarehouseDto,
    val notes: String,
    val shippingType: ShippingTypeDto?,
    val controlPoint: ControlPointDto?,
    val collectPoint: String,
    val workActivity: WorkActivityDto?,
    val documentNo: String,
    val documentDate: String,
):Parcelable