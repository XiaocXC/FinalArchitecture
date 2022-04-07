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
import kotlin.coroutines.coroutineContext

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
     * 如果你需要颗粒度更细的加载状态，例如Paging3等内容的状态，可以自行处理
     */
    protected val _rootViewState = MutableSharedFlow<UiModel<Any>>()
    val rootViewState: SharedFlow<UiModel<Any>> =  _rootViewState

    /**
     * 刷新
     * @param resetState 是否重置加载状态
     * 如果重置加载状态，那么会将整个布局的状态更改为加载状态
     */
    fun toRefresh(resetState: Boolean = true){
        if(resetState){
            _rootViewState.tryEmit(UiModel.Loading())
        }
        // 调用重写子类的刷新方法
        refresh()
    }

    /**
     * 刷新重写方法
     * 该方法仅供ViewModel中使用
     */
    protected abstract fun refresh()

    /**
     * 普通的协程请求
     * @param requestAction 请求行为函数
     * 需要返回一个由ApiResult包裹的数据集
     * @param successBlock （可选）成功后所做的操作
     * @param failureBlock （可选）失败后所做的操作
     */
    protected suspend fun <T> launchRequestByNormal(
        requestAction: suspend CoroutineContext.() -> ApiResult<T>,
        successBlock: suspend CoroutineContext.(T) -> Unit = {},
        failureBlock: suspend CoroutineContext.(Error) -> Unit = {}
    ){
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