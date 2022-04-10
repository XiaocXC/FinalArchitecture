package com.zjl.finalarchitecture.module.home.repository.datasouce

import com.zjl.base.ApiResult
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.PageVO
import com.zjl.finalarchitecture.module.home.repository.IntegerPagingSource
import com.zjl.finalarchitecture.module.home.repository.resp.HomeRepository

/**
 * @author Xiaoc
 * @since 2022-04-06
 *
 * 体系文章分页数据源请求
 * @param id 对应不同体系的栏目ID
 */
class SystemArticlePagingSource(
    private val id: String
): IntegerPagingSource<ArticleListVO>() {

    override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        return HomeRepository.requestSystemListData(currentPage, id)
    }
}