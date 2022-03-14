package com.zjl.finalarchitecture.api

import com.zjl.base.ApiResult
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.model.PageVO
import com.zjl.finalarchitecture.module.home.model.system.SystemVO
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Xiaoc
 * @since 2022-01-08
 *
 * 文章相关Service
 */
interface HomeService {

    /**
     * 获取文章栏目相关Banner数据
     */
    @GET("banner/json")
    suspend fun getBanner(): ApiResult<List<BannerVO>>

    /**
     * 获取文章列表
     */
    @GET("/article/list/{pageNum}/json")
    suspend fun getArticleListByPage(@Path("pageNum") pageNum: Int): ApiResult<PageVO<ArticleListVO>>


    /**
     * 获取置顶文章集合数据
     */
    @GET("article/top/json")
    suspend fun getTopArticleList(): ApiResult<ArrayList<ArticleListVO>>


    /**
     * 获取广场文章列表
     */
    @GET("/user_article/list/{page}/json")
    suspend fun getPlazaArticleList(@Path("page") page : Int): ApiResult<PageVO<ArticleListVO>>

    /**
     * 获取体系数据
     */
    @GET("/tree/json")
    suspend fun getSystemList(): ApiResult<List<SystemVO>>


}