package com.zjl.finalarchitecture.data.respository.datasouce

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zjl.base.ApiResult
import com.zjl.finalarchitecture.data.model.PageVO
import com.zjl.base.error.ApiError
import com.zjl.base.exception.ApiException

/**
 * @author Xiaoc
 * @since 2022-02-02
 *
 * 这是一个基于Paging3的分页数据源基类
 * 为什么要写这个基类？因为对于app的分页来讲都是基于Int值进行分页的
 * 但是分页的地方有很多，不想每个分页的地方都去写一次分页逻辑，所以就在此放置一个基类
 *
 * 该类主要是用于告诉Paging3，获取到内容后，下一页（或上一页）要加载的具体逻辑和页码等内容
 */
abstract class IntegerPagingSource<V : Any> : PagingSource<Int, V>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        val nextPage = getInitPage(params)

        try {
            when (val result = loadData(nextPage)) {
                is ApiResult.Success -> {
                    // 如果服务器返回了正确数据，则我们准备下一页数据相关配置
                    // 1.取得当前分页数据
                    val data = result.data
                    // 如果页数已经over
                    val nextPageResult = if (!data.over) {
                        //以前
//                        data.currentPage
                        //现在
                        nextPage + 1
                    } else {
                        null
                    }

                    return LoadResult.Page(
                        data.dataList,
                        null,
                        nextPageResult
                    )
                }
                is ApiResult.Failure -> {
                    return LoadResult.Error(
                        ApiException(result.error)
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(
                ApiException(ApiError.unknownError)
            )
        }
    }

    /**
     * 这里是Paging提供刷新时返回的Key
     * 如果要全刷，一般就返回null
     * 但是如果需要类似Bilibili那种刷新效果，我们就需要根据不同的状态，返回刷新时返回的Key
     * 返回的这个Key就会成为[load]的参数
     */
    override fun getRefreshKey(state: PagingState<Int, V>): Int? = null


    /**
     * 调用Data的API接口，返回对应内容
     * 这才是真正需要重写的网络请求数据的方法
     */
    abstract suspend fun loadData(currentPage: Int): ApiResult<PageVO<V>>


    open fun getInitPage(params: LoadParams<Int>): Int {
        // 获取当前页码，如果为空就是默认第1页
        return params.key ?: 0
    }


}