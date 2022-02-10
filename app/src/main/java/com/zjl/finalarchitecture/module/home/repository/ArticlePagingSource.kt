package com.zjl.finalarchitecture.module.home.repository

import com.zjl.base.ApiResult
import com.zjl.base.onSuccess
import com.zjl.base.utils.CacheUtil
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.PageVO

/**
 * @author Xiaoc
 * @since 2022-02-10
 *
 * 文章分页数据源请求
 */
class ArticlePagingSource: IntegerPagingSource<ArticleListVO>() {

    override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        val articlePageData = HomeRepository.requestArticleDataByPage(currentPage)
        //如果App配置打开了首页请求置顶文章(为什么做这个，我们可以再设置页面灵活开关)，且是第一页，则额外请求一个置顶文章列表
        if (CacheUtil.isNeedTop() && currentPage == 0) {
            val topArticleList = mutableListOf<ArticleListVO>()

            val topArticleListData = HomeRepository.requestTopArticleData()
            topArticleListData.onSuccess {
                topArticleList.addAll(it)
            }
            return articlePageData.onSuccess {
                it.dataList.addAll(0, topArticleList)
            }
        } else {
            return articlePageData
        }
    }
}