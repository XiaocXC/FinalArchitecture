package com.zjl.base.viewmodel

import androidx.lifecycle.ViewModel
import com.zjl.base.*
import com.zjl.base.error.Error
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.UiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.jvm.Throws

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
     * 请求基于ApiResult的网络请求
     *
     * example 1.
     * 我需要请求一个数据，并将数据以及它的请求状态填充到stateFlow中
     *  // 文章详情
     *  private val _articleData = MutableStateFlow<UiModel<ArticleListVO>>(UiModel.Loading())
     *  val articleData: StateFlow<UiModel<ArticleListVO>> = _articleData
     *
     *  fun test(){
     *      // 在协程作用域下启动（这里推荐requestScope，它能处理一切异常）
     *      requestScope {
     *          requestApiResult(uiModel = _articleData){
     *              ApiService.requestData()
     *          }.await()
     *      }
     *  }
     *
     *
     * example 2.
     * 我需要请求一个数据，但我不需要自行填充，我自己处理返回数据
     *
     *  fun test(){
     *      // 在协程作用域下启动（这里推荐requestScope，它能处理一切异常）
     *      requestScope {
     *          val result = requestApiResult {
     *              ApiService.requestData()
     *          }.await()
     *
     *          // 自行处理result
     *      }
     *  }
     *
     * @param uiModel UiModel的状态值，如果不传，你可以通过返回值自行处理
     * @param block 请求的接口调用
     *
     */
    @Throws(ApiException::class)
    inline fun <reified T> CoroutineScope.requestApiResult(
        uiModel: MutableSharedFlow<UiModel<T>>? = null,
        crossinline block: suspend (CoroutineScope.() -> ApiResult<T>)
    ): Deferred<T> {
        return async {
            ensureActive()
            // 如果传入了uiModel，则请求前更改为加载中状态
            uiModel?.emit(UiModel.Loading())
            return@async when(val result = block(this)){
                is ApiResult.Success ->{
                    val data = result.data
                    // 如果传入了uiModel，则给它赋值成功状态
                    uiModel?.emit(UiModel.Success(data))
                    // 返回脱壳数据
                    data
                }
                is ApiResult.Failure ->{
                    val exception = ApiException(result.error)
                    // 如果传入了uiModel，则给它赋值失败状态
                    uiModel?.emit(UiModel.Error(exception))
                    // 抛出错误，让外部去捕捉
                    throw exception
                }
            }
        }
    }

    /**
     * 普通的协程请求
     * @param requestAction 请求行为函数
     * 需要返回一个由ApiResult包裹的数据集
     * @param successBlock （可选）成功后所做的操作
     * @param failureBlock （可选）失败后所做的操作
     *
     * 该方法只提供[successBlock]和[failureBlock]回调，具体内容需要你自行处理
     */
    @Deprecated("建议使用更先进的API", replaceWith = ReplaceWith("this.requestApiResult"))
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
    @Deprecated("建议使用更先进的API", replaceWith = ReplaceWith("this.requestApiResult"))
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