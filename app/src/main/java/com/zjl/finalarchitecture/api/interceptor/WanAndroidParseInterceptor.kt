package com.zjl.finalarchitecture.api.interceptor

import com.zjl.base.globalContext
import com.zjl.finalarchitecture.R
import com.zjl.base.exception.ConvertException
import com.zjl.base.exception.ResponseException
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 这是WanAndroid（玩安卓）的业务解析拦截器
 *
 * WanAndroid规定当errorCode字段为0时代表业务成功，其余情况均为业务失败
 * 我们在这个拦截器中便做了这个业务判断，如果字段不为0，全部抛出错误
 *
 * PS. 你可以不使用这个拦截器，但是一旦你不使用，那么后台返回的错误内容你将会在每个返回内容中单独判断是否成功还是失败
 */
class WanAndroidParseInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if(!response.isSuccessful){
            return response
        }

        /**
         * 因为response.body().string() 只能调用一次
         * 所以这里读取responseBody不使用response.body().string()，
         * 原因：https://juejin.im/post/6844903545628524551
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
            throw ConvertException(response, cause = e)
        }
        // 玩安卓返回的数据中：
        // 如果没有返回类型没有errorCode字段，说明服务器返回的类型有误，直接抛出异常走 onFailure 方法
        if(!jsonObject.has("errorCode")){
            throw ConvertException(response)
        }

        // 找到errorCode字段，如果是0，则代表成功，否则也抛出异常
        // 该异常的信息为服务返回的错误信息
        val errorCode = jsonObject.optInt("errorCode")
        if(errorCode == 0){
            // errorCode为0，说明业务成功，我们不处理
            return response
        }

        // 如果errorCode不为0，则代表这次请求后台给了错误信息
        // 我们抛出错误，并把后台给的错误信息放入错误信息中
        val errorMessage = jsonObject.optString("errorMsg", globalContext.getString(R.string.network_description_unknown_error))
        // 我们把返回的errorCode作为tag放入到错误信息中
        throw ResponseException(response, message = errorMessage, tag = errorCode)
    }
}