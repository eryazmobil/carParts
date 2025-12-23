package eryaz.software.carParts.data.models.remote.response

import com.google.gson.annotations.SerializedName
import eryaz.software.carParts.data.models.dto.CurrentUserDto

data class WorkActionResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("workActivity")
    val workActivity: WorkActivityResponse,

    @SerializedName("workActionType")
    val workActionType: WorkActivityTypeResponse,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("processUser")
    val processUser: CurrentUserDto,

    @SerializedName("finishDate")
    val finishDate: String,

    @SerializedName("isFinished")
    val isFinished: Boolean
    )