package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("encryptedAccessToken")
    val encryptedAccessToken: String,

    @SerializedName("expireInSeconds")
    val expireInSeconds: Int,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("companyId")
    val companyId: Int,

    @SerializedName("warehouseId")
    val warehouseId: Int,

    @SerializedName("printPointName")
    val printPointName: String
    )