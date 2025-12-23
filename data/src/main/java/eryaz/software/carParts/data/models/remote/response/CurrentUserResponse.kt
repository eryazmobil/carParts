package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class CurrentUserResponse(
    @SerializedName("companyId")
    val companyId: Int,

    @SerializedName("warehouseId")
    val warehouseId: Int,

    @SerializedName("emailAddress")
    val emailAddress: String,

    @SerializedName("id")
    val userId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("surname")
    val surname: String,

    @SerializedName("printPointName")
    val printPointName: String,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("userShelfType")
    val userShelfType: String
)