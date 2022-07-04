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
@Deprecated("已废弃，Paging3目前被放弃使用")
class ArticlePagingSource: IntegerPagingSource<ArticleListVO>() {

    override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        val articlePageData = ApiRepository.requestArticleDataByPage(currentPage)
        //如果App配置打开了首页请求置顶文章(为什么做这个，我们可以再设置页面灵活开关)，且是第一页，则额外请求一个置顶文章列表
        return if (CacheUtil.isNeedTop() && currentPage == 0) {
            val topArticleList = mutableListOf<ArticleListVO>()

            val topArticleListData = ApiRepository.requestTopArticleData()
            topArticleListData.onSuccess {
                topArticleList.addAll(it)
            }
            articlePageData.onSuccess {
                it.dataList.addAll(0, topArticleList)
            }
        } else {
            articlePageData
        }
    }
}