package com.zjl.finalarchitecture.module.home.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zjl.finalarchitecture.module.home.model.PageVO
import com.zjl.library_network.ApiResult
import com.zjl.library_network.error.ApiError
import com.zjl.library_network.exception.ApiException

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
abstract class IntegerPagingSource<V: Any>: PagingSource<Int, V>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        // 获取当前页码，如果为空就是默认第1页
        val nextPage = params.key ?: 0

        try {
            when(val result = loadData(nextPage)){
                is ApiResult.Success ->{
                    // 如果服务器返回了正确数据，则我们准备下一页数据相关配置
                    // 1.取得当前分页数据
                    val data = result.data
                    // 如果页数已经over
                    val nextPage = if(data.over){
                        data.currentPage + 1
                    } else {
                        null
                    }

                    return LoadResult.Page(
                        data.dataList,
                        null,
                        nextPage
                    )
                }
                is ApiResult.Failure ->{
                    return LoadResult.Error(
                        ApiException(result.error)
                    )
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
            return LoadResult.Error(
                ApiException(ApiError.unknownError)
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, V>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.minus(1) ?: anchorPage?.nextKey?.plus(1)
        }
    }


    /**
     * 调用Data的API接口，返回对应内容
     */
    abstract suspend fun loadData(currentPage: Int): ApiResult<PageVO<V>>
}