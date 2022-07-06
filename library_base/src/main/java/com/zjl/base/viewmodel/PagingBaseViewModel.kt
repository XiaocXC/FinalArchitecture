package com.zjl.base.viewmodel

import com.zjl.base.ApiResult
import com.zjl.base.error.Error
import com.zjl.base.onFailure
import com.zjl.base.onSuccess
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * @author Xiaoc
 * @since  2022-07-04
 *
 * 基于分页的ViewModel，内部可以自动处理分页相关逻辑
 * 重写后只需要关心网络请求相关内容
 * 内部维护一个 [currentPageIndex] 用来记录当前页码
 *
 * 你可以重写[pageSize]方法和[initPageIndex]来规定请求页码大小和初始页码索引大小
 * 同时提供[launchRequestByPaging]等分页请求方法，让你专心处理网络逻辑
 **/
abstract class PagingBaseViewModel: BaseViewModel() {

    companion object {
        /**
         * 默认一页15
         */
        const val DEFAULT_PAGE_SIZE = 15
    }


    open fun pageSize() = 15

    open fun initPageIndex() = 0

    val pageSize = pageSize()

    var currentPageIndex = initPageIndex()
    protected set


    final override fun refresh() {
        // 重置页码数
        currentPageIndex = initPageIndex()
        loadMore()
    }

    fun loadMore(){
        loadMoreInner(currentPageIndex)
    }


    protected abstract fun loadMoreInner(currentIndex: Int)

    /**
     * 分页的协程请求
     * @param requestAction 请求行为函数
     * 需要返回一个由ApiResult包裹的数据集
     * @param successBlock （可选）成功后所做的操作
     * @param failureBlock （可选）失败后所做的操作
     *
     * 提示：该方法在请求成功后，会自动处理[currentPageIndex]的值
     * 该方法只提供[successBlock]和[failureBlock]回调，具体内容需要你自行处理
     */
    protected suspend fun <T> launchRequestByPaging(
        requestAction: suspend CoroutineContext.() -> ApiResult<T>,
        successBlock: suspend CoroutineContext.(T) -> Unit = {},
        failureBlock: suspend CoroutineContext.(Error) -> Unit = {}
    ){
        runCatching {
            val result = requestAction(coroutineContext)
            result.onSuccess {
                // 成功后当前页码 +1
                currentPageIndex ++
                // 回调成功方法
                successBlock(coroutineContext, it)
            }.onFailure {
                // 回调失败方法
                failureBlock(coroutineContext,it)
            }
        }.onFailure {
            it.printStackTrace()
            // 回调失败方法
            failureBlock(coroutineContext, Error(errorCode = 0, errorMsg = it.message))
        }
    }
}