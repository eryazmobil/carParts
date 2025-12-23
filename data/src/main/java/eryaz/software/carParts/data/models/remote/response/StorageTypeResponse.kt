package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName

data class StorageTypeResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("definition")
    val definition: String,
    @SerializedName("id")
    val id: Int
)