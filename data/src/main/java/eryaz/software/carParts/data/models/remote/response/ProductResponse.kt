package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("company")
    val company: CompanyResponse?,
    
    @SerializedName("code")
    val code: String,

    @SerializedName("definition")
    val definition: String?,

    @SerializedName("definition2")
    val definition2: String?,

    @SerializedName("manufacturer")
    val manufacturer: String?,

    @SerializedName("manufacturerCode")
    val manufacturerCode: String?,

    @SerializedName("unit")
    val unit: String?,

    @SerializedName("sizeWidth")
    val sizeWidth: Double,

    @SerializedName("sizeLength")
    val sizeLength: Double,

    @SerializedName("sizeHeight")
    val sizeHeight: Double,

    @SerializedName("weight")
    val weight: Double,

    @SerializedName("volume")
    val volume: Double,

    @SerializedName("hasSerial")
    val hasSerial: Boolean,

    @SerializedName("id")
    val id: Int
)
