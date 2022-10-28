package com.zjl.base.viewmodel

import androidx.lifecycle.ViewModel
import com.zjl.base.*
import com.zjl.base.error.Error
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.UiModel
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类ViewModel，提供基本的封装内容进行统一
 * 规定一个Fragment要基于ViewBinding
 * 根据MVI或MVVM规定，一个界面由一个ViewModel所绑定，我们这里需要一个VM的绑定
 */
abstract class BaseViewModel: ViewModel(){

    /**
     * 刷新重写方法
     * 该方法仅供ViewModel中使用
     */
    abstract fun initData()

    /**
     * 普通的协程请求
     * @param requestAction 请求行为函数
     * 需要返回一个由ApiResult包裹的数据集
     * @param successBlock （可选）成功后所做的操作
     * @param failureBlock （可选）失败后所做的操作
     *
     * 该方法只提供[successBlock]和[failureBlock]回调，具体内容需要你自行处理
     */
    protected suspend fun <T> launchRequestByNormal(
        requestAction: suspend CoroutineContext.() -> ApiResult<T>,
        successBlock: suspend CoroutineContext.(T) -> Unit = {},
        failureBlock: suspend CoroutineContext.(Error) -> Unit = {}
    ){
        runCatching {
            val result = requestAction(coroutineContext)
            result.onSuccess {
                // 回调成功方法
                successBlock(coroutineContext, it)
            }.onFailure {
                // 回调失败方法
                failureBlock(coroutineContext, it)
            }
        }.onFailure {
            it.printStackTrace()
            // 回调失败方法
            failureBlock(coroutineContext, Error(errorCode = 0, errorMsg = it.message))
        }
    }


    /**
     * 普通的协程请求（联动界面状态）
     * @param requestAction 请求行为函数
     * 需要返回一个由ApiResult包裹的数据集
     * @param resultState 用来承载请求结果的StateFlow
     * @param isShowLoading 是否显示加载联动状态，默认不处理
     * @param successBlock 成功回调，你可以在此处做自己的其他操作
     * @param failureBlock 失败回调，你可以在此处做自己的其他操作
     *
     * 该方法与[launchRequestByNormal]不同的是，它会帮助你处理Ui状态
     * 如果[isShowLoading]为true，它会帮你处理请求过程中（加载中）的状态更新
     */
    protected suspend fun <T> launchRequestByNormalWithUiState(
        requestAction: suspend CoroutineContext.() -> ApiResult<T>,
        resultState: MutableSharedFlow<UiModel<T>>,
        isShowLoading: Boolean = false,
        successBlock: suspend CoroutineContext.(T) -> Unit = {},
        failureBlock: suspend CoroutineContext.(Error) -> Unit = {}
    ){
        runCatching {
            // 如果要显示状态，设置为加载中
            if(isShowLoading){
                resultState.emit(UiModel.Loading())
            }

            val result = requestAction(coroutineContext)
            result.onSuccess { data ->
                // 如果要显示状态，设置为成功
                resultState.emit(UiModel.Success(data))
                // 回调成功方法
                successBlock(coroutineContext, data)

            }.onFailure { error ->
                // 如果要显示状态，设置为失败
                resultState.emit(UiModel.Error(ApiException(error)))
                // 回调失败方法
                failureBlock(coroutineContext, error)
            }

        }.onFailure {
            // 如果要显示状态，设置为失败
            val error = Error(errorCode = 0, errorMsg = it.message)
            resultState.emit(UiModel.Error(ApiException(error)))
            // 回调失败方法
            failureBlock(coroutineContext, error)
        }
    }
}