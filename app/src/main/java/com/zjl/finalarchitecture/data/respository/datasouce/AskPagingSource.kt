package com.zjl.finalarchitecture.data.respository.datasouce

import com.zjl.base.ApiResult
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.PageVO
import com.zjl.finalarchitecture.data.respository.ApiRepository

/**
 * @description:
 * @author: zhou
 * @date : 2022/4/11 17:22
 */
@Deprecated("已废弃，Paging3目前被放弃使用")
class AskPagingSource : IntegerPagingSource<ArticleListVO>() {

    override fun getInitPage(params: LoadParams<Int>): Int {
        return params.key ?: 1
    }

    override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        return ApiRepository.requestAskArticleListDataByPage(currentPage)
    }
}