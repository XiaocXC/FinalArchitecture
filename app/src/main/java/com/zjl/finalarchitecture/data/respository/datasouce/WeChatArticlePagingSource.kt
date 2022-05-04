package com.zjl.finalarchitecture.data.respository.datasouce

import com.zjl.base.ApiResult
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.PageVO
import com.zjl.finalarchitecture.data.respository.ApiRepository

class WeChatArticlePagingSource(
    private val id: Int
) : IntegerPagingSource<ArticleListVO>() {

    override fun getInitPage(params: LoadParams<Int>): Int {
        return params.key ?: 1
    }

    override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        return ApiRepository.requestWechatDetailListDataByPage(id, currentPage)
    }

}