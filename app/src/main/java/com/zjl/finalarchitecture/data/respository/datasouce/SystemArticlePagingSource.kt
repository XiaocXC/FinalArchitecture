package com.zjl.finalarchitecture.data.respository.datasouce

import com.zjl.base.ApiResult
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.PageVO
import com.zjl.finalarchitecture.data.respository.ApiRepository

/**
 * @author Xiaoc
 * @since 2022-04-06
 *
 * 体系文章分页数据源请求
 * @param id 对应不同体系的栏目ID
 */
@Deprecated("已废弃，Paging3目前被放弃使用")
class SystemArticlePagingSource(
    private val id: String
): IntegerPagingSource<ArticleListVO>() {

    override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        return ApiRepository.requestSystemListData(currentPage, id)
    }
}