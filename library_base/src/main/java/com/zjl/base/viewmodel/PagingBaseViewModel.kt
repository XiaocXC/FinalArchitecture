package com.zjl.base.viewmodel

/**
 * @author Xiaoc
 * @since  2022-07-04
 *
 * 基于分页的ViewModel
 * 重写后你可以根据规范进行分页请求
 * 当然，你可以不使用它，直接使用BaseViewModel也是可以的
 *
 * 你可以重写[pageSize]方法和[initPageIndex]方法来规定请求页码大小和初始页码索引大小
 **/
abstract class PagingBaseViewModel: BaseViewModel() {

    open fun pageSize(): Int{
        return 20
    }

    open fun initPageIndex(): Int{
        return 0
    }

    /**
     * 刷新分页数据
     * 该方法你应该重置当前页码，并请求数据
     * @param tag 如果存在多个分页请求，可以用该tag进行区分
     */
    abstract fun onRefreshData(tag: Any? = null)

    /**
     * 加载更多数据
     * 该方法你加载更多分页数据
     * @param tag 如果存在多个分页请求，可以用该tag进行区分
     */
    abstract fun onLoadMoreData(tag: Any? = null)
}