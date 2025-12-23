package eryaz.software.carParts.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class UnAuthorizedInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            400 -> {

            }
            401 -> {
//                SessionManager.logOut()
            }
        }
        return response
    }
}
