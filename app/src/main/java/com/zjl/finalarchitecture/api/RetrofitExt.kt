package com.zjl.finalarchitecture.api

import com.zjl.lib_base.BuildConfig
import com.zjl.library_network.calladapter.ApiResultCallAdapterFactory
import com.zjl.library_network.interceptor.BusinessErrorInterceptor
import com.zjl.base.utils.globalJson
import com.zjl.finalarchitecture.utils.network.HeaderInterceptor
import com.zjl.library_network.converter.asConverterFactory
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
    // 添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
    addInterceptor(HeaderInterceptor())
    // 添加Log日志记录
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