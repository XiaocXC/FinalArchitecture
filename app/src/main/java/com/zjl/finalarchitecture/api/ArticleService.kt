package com.zjl.finalarchitecture.api

import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.model.PageVO
import com.zjl.library_network.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Xiaoc
 * @since 2022-01-08
 *
 * 文章相关Service
 */
interface ArticleService {

    /**
     * 获取文章栏目相关Banner数据
     */
    @GET("banner/json")
    suspend fun getBanner(): ApiResult<List<BannerVO>>

    /**
     * 获取文章栏目相关Banner数据
     */
    @GET("/article/list/{pageNum}/json")
    suspend fun getArticleListByPage(
        @Path("pageNum") pageNum: Int
    ): ApiResult<PageVO<ArticleListVO>>
}