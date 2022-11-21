package com.zjl.library_network.utils.okhttp.interceptor

import com.zjl.library_network.utils.okhttp.ext.downloadListeners
import com.zjl.library_network.utils.okhttp.ext.toExtRequestBody
import com.zjl.library_network.utils.okhttp.ext.toExtResponseBody
import com.zjl.library_network.utils.okhttp.ext.uploadListeners
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author Xiaoc
 * @since  2022-11-18
 *
 * 这是上传下载进度监听的ProgressHandler拦截器
 **/
class ProgressHandlerInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        // 添加上传进度监听的RequestBody
        val reqBody = request.body?.toExtRequestBody(request.uploadListeners())
        request = request.newBuilder().method(request.method, reqBody).build()

        val response = chain.proceed(request)
        // 添加下载进度监听的ResponseBody
        val respBody = response.body?.toExtResponseBody(request.downloadListeners())
        return response.newBuilder().body(respBody).build()
    }
}