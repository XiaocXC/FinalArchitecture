package com.zjl.finalarchitecture.module.home.repository

import com.zjl.base.ApiResult
import com.zjl.base.onSuccess
import com.zjl.base.ui.onSuccess
import com.zjl.finalarchitecture.api.ArticleService
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.model.PageVO
import com.zjl.library_network.client.mRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * @author Xiaoc
 * @since 2022-01-08
 *
 * 首页相关内容 仓库层
 */
object HomeRepository {

    private val mArticleService by lazy { mRetrofit.create(ArticleService::class.java) }

    /**
     * 获取Banner数据
     * @return Banner列表
     */
    suspend fun requestBanner(): ApiResult<List<BannerVO>> {
        return withContext(Dispatchers.IO){
            mArticleService.getBanner()
        }
    }

    /**
     * 获取文章分页数据
     * 该请求会在分页最开始查询一次置顶文章
     * @return 文章列表
     */
    suspend fun requestArticleByPageData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        return withContext(Dispatchers.IO){
            // 判断如果是第一页，则查询置顶文章
            val topArticleList = mutableListOf<ArticleListVO>()
            if(currentPage == 0){
                val topArticleDeferred = async {
                    requestTopArticleData()
                }
                topArticleDeferred.await().onSuccess {
                    topArticleList.addAll(it)
                }
            }

            // 分页查询文章
            val articleListDeferred = async {
                mArticleService.getArticleListByPage(currentPage)
            }
            return@withContext articleListDeferred.await().onSuccess {
                it.dataList.addAll(0, topArticleList)
            }
        }
    }


    /**
     * 获取置顶文章
     * @return 置顶文章列表
     */
    private suspend fun requestTopArticleData(): ApiResult<List<ArticleListVO>>{
        return mArticleService.getTopArticleList()
    }
}