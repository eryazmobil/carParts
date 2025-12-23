package eryaz.software.carParts.data.api.client

import android.content.Context
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import eryaz.software.carParts.data.BuildConfig
import eryaz.software.carParts.data.api.interceptors.AuthorizationInterceptor
import eryaz.software.carParts.data.api.interceptors.HttpLoggingInterceptor
import eryaz.software.carParts.data.api.interceptors.UnAuthorizedInterceptor
import eryaz.software.carParts.data.api.services.*
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ZeusClient {
    fun provideApi(context: Context): AuthApiService {
        return getRetrofit(context).create(AuthApiService::class.java)
    }

    fun provideUserApi(context: Context): UserApiService {
        return getRetrofit(context).create(UserApiService::class.java)
    }

    fun provideWorkActivityApi(context: Context): WorkActivityService {
        return getRetrofit(context).create(WorkActivityService::class.java)
    }

    fun provideBarcodeApi(context: Context): BarcodeService {
        return getRetrofit(context).create(BarcodeService::class.java)
    }

    fun providePlacementApi(context: Context): PlacementService {
        return getRetrofit(context).create(PlacementService::class.java)
    }

    fun provideOrderApi(context: Context): OrderService {
        return getRetrofit(context).create(OrderService::class.java)
    }

    fun provideCountingApi(context: Context): CountingService {
        return getRetrofit(context).create(CountingService::class.java)
    }

    private fun getRetrofit(context: Context): Retrofit {
        val client = OkHttpClient.Builder()
            .protocols(listOf(Protocol.HTTP_1_1))
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(UnAuthorizedInterceptor())
            .addInterceptor(AuthorizationInterceptor())
            .addNetworkInterceptor(HttpLoggingInterceptor.getInterceptor())
            .build()

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_OUT_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}