package com.zjl.finalarchitecture.utils

import com.zjl.library_network.ApiResult
import com.zjl.library_network.error.Error
import com.zjl.library_network.onFailure
import com.zjl.library_network.onSuccess
import kotlinx.coroutines.CoroutineScope

/**
 * 普通的协程请求
 * @param requestAction 请求行为函数
 * 需要返回一个由ApiResult包裹的数据集
 * @param successBlock （可选）成功后所做的操作
 * @param failureBlock （可选）失败后所做的操作
 */
inline fun <T> CoroutineScope.requestByNormal(
    requestAction: CoroutineScope.() -> ApiResult<T>,
    successBlock: (T) -> Unit = {},
    failureBlock: (Error) -> Unit = {}
){
    val result = requestAction()
    result.onSuccess {
        successBlock(it)
    }.onFailure {
        failureBlock(it)
    }
}