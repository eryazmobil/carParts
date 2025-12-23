package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

class ShelfResponse(

    @SerializedName("fullAddress")
    val fullAddress: String,

    @SerializedName("quantity")
    val quantity: Double,

    @SerializedName("id")
    val shelfId: Int
)