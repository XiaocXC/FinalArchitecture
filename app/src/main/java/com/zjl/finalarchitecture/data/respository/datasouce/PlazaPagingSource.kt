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
class PlazaPagingSource:IntegerPagingSource<ArticleListVO>() {
    override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        return ApiRepository.requestPlazaArticleDataByPage(currentPage)
    }
}