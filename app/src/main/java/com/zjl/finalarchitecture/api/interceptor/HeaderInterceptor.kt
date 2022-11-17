package com.zjl.finalarchitecture.api.interceptor

import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 所有网络请求公共头拦截器
 * 可以将登录等信息添加在请求头部
 **/
class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if(UserAuthDataSource.isLogin){
            builder.addHeader("token", UserAuthDataSource.basicUserInfoVO.value.token).build()
            builder.addHeader("device", "Android").build()
        }
        builder.addHeader("isLogin", UserAuthDataSource.isLogin.toString()).build()
        return chain.proceed(builder.build())
    }

}