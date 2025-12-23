package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class WorkActivityResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("code")
    val code: String,
    @SerializedName("workActivityType")
    val workActivityTypeResponse: WorkActivityTypeResponse?,
    @SerializedName("client")
    val client: ClientSmallResponse?,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("finishDate")
    val finishDate: String,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("deleterUserId")
    val deleterUserId: Int?,
    @SerializedName("deletionTime")
    val deletionTime: String?,
    @SerializedName("lastModificationTime")
    val lastModificationTime: String,
    @SerializedName("lastModifierUserId")
    val lastModifierUserId: Int,
    @SerializedName("creationTime")
    val creationTime: String,
    @SerializedName("creatorUserId")
    val creatorUserId: Int,
    @SerializedName("isLocked")
    val isLocked: Boolean,
    @SerializedName("company")
    val company: CompanyResponse?,
    @SerializedName("shippingType")
    val shippingType: ShippingTypeResponse?,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("documentNoStr")
    val documentNoStr: String?,
    @SerializedName("controlPointDefinition")
    val controlPointDefinition: String?,
    @SerializedName("hasPriority")
    val hasPriority: Boolean
)

