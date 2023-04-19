package com.zjl.finalarchitecture.api

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.zjl.base.globalContext
import com.zjl.finalarchitecture.api.converter.kotlin.WanAndroidSerializer
import com.zjl.library_network.converter.kotlin.SerializerFactory
import com.zjl.base.BuildConfig
import com.zjl.library_network.calladapter.ApiResultCallAdapterFactory
import com.zjl.finalarchitecture.api.interceptor.WanAndroidParseInterceptor
import com.zjl.library_network.utils.globalJson
import com.zjl.finalarchitecture.api.interceptor.HeaderInterceptor
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

val wanAndroidCookieJar: PersistentCookieJar by lazy {
    PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(globalContext))
}

private val okHttpClient = OkHttpClient.Builder().apply {
    // 添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
    addInterceptor(HeaderInterceptor())
    // 添加Log日志记录
    if(BuildConfig.DEBUG){
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }
}.addInterceptor(WanAndroidParseInterceptor())
    //添加Cookies自动持久化
    .cookieJar(wanAndroidCookieJar)
    .callTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

/**
 * 玩安卓的Retrofit
 * 我们建议不同域名使用不同的Retrofit进行请求
 * 当然你也可以不用
 */
val mWanAndroidRetrofit = Retrofit.Builder()
    // 这是一个要求你的返回值类型都是ApiResult的适配器，如果你不加上它，那么请求结果不会以ApiResult包装
    // 并且请求出现的错误需要你自己去捕捉
    .addCallAdapterFactory(ApiResultCallAdapterFactory())
    .addConverterFactory(SerializerFactory("application/json".toMediaType(), WanAndroidSerializer(globalJson)))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()