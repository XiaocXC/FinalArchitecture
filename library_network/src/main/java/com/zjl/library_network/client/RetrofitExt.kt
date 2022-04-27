package com.zjl.library_network.client

import com.zjl.lib_base.BuildConfig
import com.zjl.library_network.calladapter.ApiResultCallAdapterFactory
import com.zjl.library_network.converter.asConverterFactory
import com.zjl.library_network.interceptor.BusinessErrorInterceptor
import com.zjl.library_network.utils.globalJson
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
private const val BASE_URL = "https://www.wanandroid.com"

private val okHttpClient = OkHttpClient.Builder().apply {
    if(BuildConfig.DEBUG){
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }
}.addInterceptor(BusinessErrorInterceptor())
    .callTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

val mRetrofit = Retrofit.Builder()
    .addCallAdapterFactory(ApiResultCallAdapterFactory())
    .addConverterFactory(globalJson.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()