package eryaz.software.carParts.data.models.remote.request

import com.google.gson.annotations.SerializedName

data class ProductShelfUpdateRequest(

    @SerializedName("productShelfId")
    val productShelfId: Int,
    @SerializedName("safetyPercent")
    val safetyPercent: Int,

    @SerializedName("maxQuantity")
    val maxQuantity: Int
    )