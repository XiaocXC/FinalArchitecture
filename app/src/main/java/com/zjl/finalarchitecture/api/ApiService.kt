package com.zjl.finalarchitecture.api

import com.zjl.base.ApiResult
import com.zjl.finalarchitecture.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Xiaoc
 * @since 2022-01-08
 *
 * 文章相关Service
 */
interface ApiService {

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
    suspend fun getPlazaArticleListByPage(@Path("page") page : Int): ApiResult<PageVO<ArticleListVO>>

    /**
     * 获取体系数据
     */
    @GET("tree/json")
    suspend fun getSystemList(): ApiResult<List<SystemVO>>

    /**
     * 知识体系下的文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getSystemChildData(
        @Path("page") pageNo: Int,
        @Query("cid") id: String
    ): ApiResult<PageVO<ArticleListVO>>

    /**
     * 获取导航数据
     */
    @GET("navi/json")
    suspend fun getNavigationList(): ApiResult<List<NavigationVO>>


}