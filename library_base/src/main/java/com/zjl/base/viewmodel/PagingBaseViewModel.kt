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
 **/
abstract class PagingBaseViewModel: BaseViewModel() {

    companion object {
        const val DEFAULT_PAGE_SIZE = 15
    }


    open fun pageSize() = DEFAULT_PAGE_SIZE

    open fun initPageIndex() = 0

    val pageSize = pageSize()

    var currentPageIndex = initPageIndex()
    protected set

    fun loadMore(){
        loadMoreInner(currentPageIndex)
    }

    final override fun refresh() {
        currentPageIndex = initPageIndex()
        loadMore()
    }

    protected abstract fun loadMoreInner(currentIndex: Int)



    /**
     * 分页的协程请求
     * @param requestAction 请求行为函数
     * 需要返回一个由ApiResult包裹的数据集
     * @param successBlock （可选）成功后所做的操作
     * @param failureBlock （可选）失败后所做的操作
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
                successBlock(coroutineContext, it)
            }.onFailure {
                failureBlock(coroutineContext,it)
            }
        }.onFailure {
            it.printStackTrace()
            failureBlock(coroutineContext, Error(errorCode = 0, errorMsg = it.message))
        }
    }
}