package eryaz.software.carParts.data.api.utils

import com.google.gson.Gson
import com.google.gson.JsonParser
import eryaz.software.carParts.data.models.remote.response.BaseResponse
import eryaz.software.carParts.data.models.remote.response.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.HttpException
import timber.log.Timber

object ResponseHandler {

    fun <T : Any> handleSuccess(baseResponse: BaseResponse, data: T?): Resource<T> {

        return if (isSuccessful(baseResponse) && data != null) {
            Resource.Success(data)
        } else {
            Resource.Error(
                message = ""
            )
        }
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        Timber.e(e)

        if (e is HttpException) {
            return handleErrorBody(body = e.response()?.errorBody()).copy()
        }

        return Resource.Error(
            message = "",
        )
    }

    private fun handleErrorBody(body: ResponseBody?): Resource.Error {
        try {
            Gson().fromJson(JsonParser.parseString(body?.string()), ErrorResponse::class.java)
                ?.let {
                    return Resource.Error(
                        message = it.error.message,
                    )
                }
        } catch (e: Exception) {
        }

        return Resource.Error(
            message = "",
        )
    }


    private fun isSuccessful(baseResponse: BaseResponse): Boolean {

        return baseResponse.success
    }
}
