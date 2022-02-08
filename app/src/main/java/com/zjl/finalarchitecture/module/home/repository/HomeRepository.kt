package com.zjl.finalarchitecture.module.home.repository

import com.zjl.base.ui.UiModel
import com.zjl.finalarchitecture.api.ArticleService
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.model.PageVO
import com.zjl.library_network.ApiResult
import com.zjl.library_network.client.mRetrofit
import com.zjl.library_network.transToUiModel
import kotlinx.coroutines.Dispatchers
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
    suspend fun requestBanner(): UiModel<List<BannerVO>> {
        return withContext(Dispatchers.IO){
            val result = mArticleService.getBanner()
            result.transToUiModel()
        }
    }

    /**
     * 获取文章分页数据
     * @return 文章列表
     */
    suspend fun requestArticleByPage(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        return withContext(Dispatchers.IO){
            mArticleService.getArticleListByPage(currentPage)
        }
    }

    suspend fun requestArticleByPageData(currentPage: Int): UiModel<PageVO<ArticleListVO>> {
        return withContext(Dispatchers.IO){
            val result =  mArticleService.getArticleListByPage(currentPage)
            result.transToUiModel()
        }
    }


    /**
     * 获取置顶文章
     * @return 置顶文章列表
     */
    suspend fun requestTopArticleData(): UiModel<List<ArticleListVO>>{
        return withContext(Dispatchers.IO){
            val result = mArticleService.getTopArticleList()
            result.transToUiModel()
        }
    }
}