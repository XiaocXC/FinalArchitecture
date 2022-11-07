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
 * 一个PagingBaseViewModel只能自动处理一个分页数据加载
 * 如果你一个界面含有多个分页，另一个分页需要你自行管理或者重新创建一个新的PagingBaseViewModel进行管理
 **/
abstract class PagingBaseViewModel: BaseViewModel() {

    open fun pageSize() = 20

    open fun initPageIndex() = 0

    abstract fun onRefreshData(tag: Any? = null)

    abstract fun onLoadMoreData(tag: Any? = null)
}