package com.zjl.base


/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 响应服务端数据返回的包装类，它主要用于与服务端进行关联
 */
sealed class ApiResult<out T> {

    data class Success<T>(val data: T): ApiResult<T>()

    data class Failure constructor(val throwable: Throwable): ApiResult<Nothing>()
}

/**
 * 如果ApiResult为Success时执行[action]自定义的操作
 * 否则不进行任何操作，返回本身
 * @param action 成功后的操作
 */
inline fun <T> ApiResult<T>.onSuccess(action: (T) -> Unit): ApiResult<T>{
    if(this is ApiResult.Success){
        action(this.data)
    }
    return this
}

/**
 * 如果ApiResult为Success时执行[action]自定义的操作
 * 否则不进行任何操作，返回本身
 * @param action 成功后的操作
 */
inline fun <T> ApiResult<T>.onFailure(action: (Throwable) -> Unit): ApiResult<T>{
    if(this is ApiResult.Failure){
        action(this.throwable)
    }
    return this
}

fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R>{
    return when(this){
        is ApiResult.Success ->{
            ApiResult.Success(transform(this.data))
        }
        is ApiResult.Failure -> {
            ApiResult.Failure(this.throwable)
        }
    }
}