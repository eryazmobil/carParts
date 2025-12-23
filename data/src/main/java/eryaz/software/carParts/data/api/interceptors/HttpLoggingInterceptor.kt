package eryaz.software.carParts.data.api.interceptors

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class HttpLoggingInterceptor {
    companion object {
        fun getInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor(ApiLogger())

            interceptor.apply {
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            }

            return interceptor
        }
    }
}

class ApiLogger : HttpLoggingInterceptor.Logger{
    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val prettyPrintJson = GsonBuilder()
                    .setPrettyPrinting()
                    .create().toJson(JsonParser.parseString(message))

                Timber.v(prettyPrintJson)
            } catch (m: JsonSyntaxException) {
                Timber.v(message)
            }
        } else {
            Timber.v(message)
            return
        }
    }
}