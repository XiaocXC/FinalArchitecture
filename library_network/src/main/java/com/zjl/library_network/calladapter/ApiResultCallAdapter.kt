package com.zjl.library_network.calladapter

import com.zjl.base.ApiResult
import com.zjl.base.exception.*
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 这是一个要求请求结果均使用ApiResult包裹的适配器
 * 在请求完成后，会把数据包装为ApiResult.Success或ApiResult.Failure
 * 从而避免了请求出现错误而你自己没有手动处理导致的闪退
 */
class ApiResultCallAdapterFactory: CallAdapter.Factory(){

    /**
     * 检查是否是 Call<ApiResult<T>> 类型的返回类型
     */
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        /**
         * 凡是检测不通过的，直接抛异常，并提示使用者返回值的类型不正确
         */
        // 检查返回类型里是否是Call<T>类型
        check(getRawType(returnType) == Call::class.java){
            "返回值必须是 retrofit2.Call 类型"
        }
        check(returnType is ParameterizedType){
            "返回至类型必须是ParameterizedType对应类型"
        }

        // 取出Call<T>里的T，检查是否是ApiResult<T>类型
        val apiResultType = getParameterUpperBound(0,returnType)
        check(getRawType(apiResultType) == ApiResult::class.java){
            "返回包装类型必须是 ApiResult 类型"
        }
        check(apiResultType is ParameterizedType){
            "返回包装类型必须是ParameterizedType对应类型"
        }

        // 取出 ApiResult<T> 中的T，这才是网络请求返回的真正类型
        val dataType = getParameterUpperBound(0,apiResultType)

        return ApiResultCallAdapter<Any>(dataType)
    }

}

/**
 * CallAdapter适配器，也就是将 T 转换为 ApiResult<T> 的适配器
 */
class ApiResultCallAdapter<T>(private val type: Type): CallAdapter<T, Call<ApiResult<T>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<T>): Call<ApiResult<T>> {
        return ApiResultCall(call)
    }

}

/**
 * 继承Call接口
 * 它相当于在其中加上了请求的封装
 * 它将从服务端传回的数据类型封装为 ApiResult<T> 再回调
 * 并进行了一些错误处理
 */
class ApiResultCall<T>(private val delegate: Call<T>): Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        delegate.enqueue(object : Callback<T> {

            /**
             * 网络请求成功后回调该方法（无论statusCode是否为200）
             */
            override fun onResponse(call: Call<T>, response: Response<T>) {
                try {
                    val code = response.code()
                    when {
                        // 请求成功
                        code in 200..299 ->{
                            val result = ApiResult.Success(response.body()!!)
                            callback.onResponse(this@ApiResultCall, Response.success(result))
                        }
                        // 400-499是客户端错误
                        code in 400..499 ->{
                            val result = ApiResult.Failure(RequestParamsException(response.raw(), code.toString()))
                            callback.onResponse(this@ApiResultCall, Response.success(result))
                        }
                        // 500以上是服务端错误
                        code >= 500 ->{
                            val result = ApiResult.Failure(ServerResponseException(response.raw(), code.toString()))
                            callback.onResponse(this@ApiResultCall, Response.success(result))
                        }
                        else ->{
                            val result = ApiResult.Failure(ConvertException(response.raw()))
                            callback.onResponse(this@ApiResultCall, Response.success(result))
                        }
                    }
                } catch (e: Throwable){
                    e.printStackTrace()
                    val result = ApiResult.Failure(ConvertException(response = response.raw(), cause = e))
                    callback.onResponse(this@ApiResultCall, Response.success(result))
                }
            }

            /**
             * 网络请求失败后回调该方法
             */
            override fun onFailure(call: Call<T>, t: Throwable) {
                Timber.d(t)
                val request = call.request()
                val failureResult = when (t) {
                    // 超时
                    is SocketTimeoutException -> {
                        ApiResult.Failure(NetworkSocketTimeoutException(request, t.message, t))
                    }
                    // 连接错误
                    is ConnectException ->{
                        ApiResult.Failure(NetworkConnectException(request, cause = t))
                    }
                    // 无法解析域名
                    is UnknownHostException -> {
                        ApiResult.Failure(NetUnknownHostException(request, message = t.message))
                    }
                    // 自定义的网络错误
                    is NetworkException ->{
                        ApiResult.Failure(t)
                    }
                    else -> {
                        // 剩下的错误全部判定为Http请求失败的错误
                        ApiResult.Failure(HttpFailureException(request, cause = t))
                    }
                }
                callback.onResponse(this@ApiResultCall, Response.success(failureResult))
            }

        })

    }

    override fun clone(): Call<ApiResult<T>> = ApiResultCall(delegate.clone())

    override fun execute(): Response<ApiResult<T>> {
        throw UnsupportedOperationException("不支持同步请求")
    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun cancel() {
        return delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }

}