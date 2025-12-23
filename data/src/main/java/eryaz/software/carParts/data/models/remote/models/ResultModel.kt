package eryaz.software.carParts.data.models.remote.models

import com.google.gson.annotations.SerializedName
import eryaz.software.carParts.data.models.remote.response.BaseResponse

data class ResultModel<T>(
    @SerializedName("result")
    val result: T
) : BaseResponse()