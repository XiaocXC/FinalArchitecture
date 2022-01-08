package com.zjl.library_network.client

import com.zjl.lib_base.BuildConfig
import com.zjl.library_network.calladapter.ApiResultCallAdapterFactory
import com.zjl.library_network.converter.asConverterFactory
import com.zjl.library_network.interceptor.BusinessErrorInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author Xiaoc
 * @since 2022-01-08
 *
 * Retrofit 客户端
 */
private const val BASE_URL = "https://www.wanandroid.com/"

private val okHttpClient = OkHttpClient.Builder().apply {
    if(BuildConfig.DEBUG){
        addInterceptor(HttpLoggingInterceptor())
    }
}.addInterceptor(BusinessErrorInterceptor())
    .callTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

val retrofit = Retrofit.Builder()
    .addCallAdapterFactory(ApiResultCallAdapterFactory())
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()