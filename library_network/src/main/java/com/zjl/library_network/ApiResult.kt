package com.zjl.library_network

import com.zjl.base.ui.UiModel
import com.zjl.library_network.error.Error
import com.zjl.library_network.exception.ApiException


/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 响应服务端数据返回的包装类，它主要用于与服务端进行关联
 */
sealed class ApiResult<out T> {

    data class Success<T>(val data: T): ApiResult<T>()

    data class Failure constructor(
        val error: Error
    ): ApiResult<Nothing>()
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
inline fun <T> ApiResult<T>.onFailure(action: (Error) -> Unit): ApiResult<T>{
    if(this is ApiResult.Failure){
        action(this.error)
    }
    return this
}

/**
 * 将ApiResult转换为UiModel
 * 如果是[ApiResult.Success]则转换为[UiModel.Success]
 * 否则转换为[UiModel.Error]
 * 避免每次写模板代码
 */
fun <T> ApiResult<T>.transToUiModel(): UiModel<T>{
    return when(this){
        is ApiResult.Success ->{
            UiModel.Success(this.data)
        }
        is ApiResult.Failure -> {
            UiModel.Error(ApiException(this.error))
        }
    }
}