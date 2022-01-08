package com.zjl.library_network.interceptor

import com.zjl.base.error.ApiError
import com.zjl.base.error.Error
import com.zjl.library_network.exception.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 业务错误的处理
 * 当出现请求成功，但是返回的内容不符合业务（转换成对于对象出错）时
 * 会抛出错误，然后走 onFailure 方法
 * 以及如果返回的success字段不为true时，也抛出异常直接走 onFailure 方法
 */
class BusinessErrorInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if(!response.isSuccessful){
            return response
        }

        /**
         * 因为response.body().string() 只能调用一次，所以这里读取responseBody不使用response.body().string()，原因：https://juejin.im/post/6844903545628524551
         * 以下读取resultString的代码节选自:
         * https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/src/main/kotlin/okhttp3/logging/HttpLoggingInterceptor.kt
         */
        val responseBody = response.body!!
        val source = responseBody.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer
        val contentType = responseBody.contentType()
        val charset: Charset = contentType?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
        val resultString = buffer.clone().readString(charset)

        val jsonObject = try {
            JSONObject(resultString)
        } catch (e: JSONException){
            throw ApiException(ApiError.dataTypeError)
        }
        // 玩安卓返回的数据中：
        // 如果没有返回类型没有errorCode字段，说明服务器返回的类型有误，直接抛出异常走 onFailure 方法
        if(!jsonObject.has("errorCode")){
            throw ApiException(ApiError.dataTypeError)
        }

        // 找到errorCode字段，如果是0，则代表成功，否则也抛出异常
        // 该异常的信息为服务返回的错误信息
        val errorCode = jsonObject.optInt("errorCode")
        if(errorCode == 0){
            return response
        }

        // 如果没有走成功的路径，则代表这次请求有问题
        // 获取服务端返回的具体错误信息
        throw ApiException(
            Error(
                errorMsg = jsonObject.optString("errorMsg"),
                errorCode = jsonObject.optInt("errorCode")
            )
        )
    }
}