package com.zjl.finalarchitecture.data.respository.datasouce

import com.zjl.base.ApiResult
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.PageVO
import com.zjl.finalarchitecture.data.respository.ApiRepository


class ProjectArticlePagingSource(
    private val id: Int
): IntegerPagingSource<ArticleListVO>() {

    override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        return ApiRepository.requestProjectDetailListDataByPage(currentPage, id)
    }
}