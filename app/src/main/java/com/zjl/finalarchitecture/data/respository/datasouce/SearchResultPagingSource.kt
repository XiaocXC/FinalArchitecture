package com.zjl.finalarchitecture.data.respository.datasouce

import com.zjl.base.ApiResult
import com.zjl.base.onSuccess
import com.zjl.finalarchitecture.utils.CacheUtil
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.PageVO
import com.zjl.finalarchitecture.data.respository.ApiRepository

/**
 * @author Xiaoc
 * @since 2022-02-10
 *
 * 文章分页数据源请求
 */
class SearchResultPagingSource(
    private val key: String
): IntegerPagingSource<ArticleListVO>() {

    override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        return ApiRepository.requestSearchDataByKey(currentPage, key)
    }
}