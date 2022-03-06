package com.zjl.finalarchitecture.module.home.repository.resp

import com.zjl.base.ApiResult
import com.zjl.base.onSuccess
import com.zjl.base.utils.CacheUtil
import com.zjl.finalarchitecture.api.HomeService
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

    private val mHomeService by lazy { mRetrofit.create(HomeService::class.java) }

    /**
     * 获取Banner数据
     * @return Banner列表
     */
    suspend fun requestBanner(): ApiResult<List<BannerVO>> {
        return withContext(Dispatchers.IO) {
            mHomeService.getBanner()
        }
    }

    /**
     * 获取文章分页数据
     * 该请求会在分页最开始查询一次置顶文章
     * @return 文章列表
     */
    suspend fun requestArticleByPageData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
        return withContext(Dispatchers.IO) {
            // 先！！分页查询文章 还没 await
            val articleListDeferred = async {
                mHomeService.getArticleListByPage(currentPage)
            }
            //如果App配置打开了首页请求置顶文章(为什么做这个，我们可以再设置页面灵活开关)，且是第一页
            if (CacheUtil.isNeedTop() && currentPage == 0) {
                val topArticleList = mutableListOf<ArticleListVO>()
                val topArticleDeferred = async { requestTopArticleData() }
                topArticleDeferred.await().onSuccess {
                    //用一个list盛放数据
                    topArticleList.addAll(it)
                }
                //然后articleList.await 并且 add 置顶文章
                return@withContext articleListDeferred.await().onSuccess {
                    it.dataList.addAll(0, topArticleList)
                }
            } else {
                return@withContext articleListDeferred.await()
            }
        }
    }


    /**
     * 获取置顶文章
     * @return 置顶文章列表
     */
    suspend fun requestTopArticleData(): ApiResult<List<ArticleListVO>> {
        return mHomeService.getTopArticleList()
    }

    /**
     * 分页获取文章
     * @param pageNo 当前页码
     */
    suspend fun requestArticleDataByPage(pageNo: Int): ApiResult<PageVO<ArticleListVO>> {
        return mHomeService.getArticleListByPage(pageNo)
    }


    /**
     * 分页获取广场文章
     * @param pageNo 当前页码
     */
    suspend fun requestPlazaArticleData(pageNo: Int): ApiResult<PageVO<ArticleListVO>> {
        return mHomeService.getPlazaArticleList(pageNo)
    }

}