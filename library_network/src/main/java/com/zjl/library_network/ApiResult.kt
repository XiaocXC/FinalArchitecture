package com.zjl.library_network

import com.zjl.library_network.error.Error


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