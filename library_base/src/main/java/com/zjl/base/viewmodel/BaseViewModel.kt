package com.zjl.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjl.base.ApiResult
import com.zjl.base.error.Error
import com.zjl.base.onFailure
import com.zjl.base.onSuccess
import com.zjl.base.ui.UiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类ViewModel，提供基本的封装内容进行统一
 * 规定一个Fragment要基于ViewBinding
 * Fragment可能会同时依赖多个ViewModel或者干脆不使用ViewModel
 * 所以我们没有在Fragment中强制规定ViewModel的使用
 */
abstract class BaseViewModel: ViewModel(){

    /**
     * 根View状态值
     * 该值为当前viewModel控制视图的根View的状态值，它存储着整个页面当前的状态
     * 该值属于视图事件，所以不能为黏性事件
     */
    protected val _rootViewState = MutableSharedFlow<UiModel<Any>>()
    val rootViewState: SharedFlow<UiModel<Any>> = _rootViewState.asSharedFlow()


    /**
     * 普通的协程请求
     * @param requestAction 请求行为函数
     * 需要返回一个由ApiResult包裹的数据集
     * @param successBlock （可选）成功后所做的操作
     * @param failureBlock （可选）失败后所做的操作
     *
     * @return Job 协程Job，可以随时取消
     */
    protected fun <T> launchRequestByNormal(
        requestAction: suspend CoroutineContext.() -> ApiResult<T>,
        successBlock: suspend CoroutineContext.(T) -> Unit = {},
        failureBlock: suspend CoroutineContext.(Error) -> Unit = {}
    ): Job{

        return viewModelScope.launch {
            runCatching {
                val result = requestAction(coroutineContext)
                result.onSuccess {
                    successBlock(coroutineContext, it)
                }.onFailure {
                    failureBlock(coroutineContext,it)
                }
            }.onFailure {
                failureBlock(coroutineContext, Error(errorCode = 0, errorMsg = it.message))
            }
        }


    }
}