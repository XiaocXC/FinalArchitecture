package com.zjl.finalarchitecture.api

import com.zjl.base.ApiResult
import com.zjl.finalarchitecture.data.model.*
import retrofit2.http.*

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
    @GET("/banner/json")
    suspend fun getBanner(): ApiResult<List<BannerVO>>

    /**
     * 获取文章列表
     */
    @GET("/article/list/{pageNum}/json")
    suspend fun getArticleListByPage(@Path("pageNum") pageNum: Int): ApiResult<PageVO<ArticleListVO>>


    /**
     * 获取置顶文章集合数据
     */
    @GET("/article/top/json")
    suspend fun getTopArticleList(): ApiResult<ArrayList<ArticleListVO>>


    /**
     * 获取广场文章列表
     */
    @GET("/user_article/list/{page}/json")
    suspend fun getPlazaArticleListByPage(@Path("page") page : Int): ApiResult<PageVO<ArticleListVO>>

    /**
     * 获取体系数据
     */
    @GET("/tree/json")
    suspend fun getSystemList(): ApiResult<List<SystemVO>>

    /**
     * 知识体系下的文章数据
     */
    @GET("/article/list/{page}/json")
    suspend fun getSystemChildData(
        @Path("page") pageNo: Int,
        @Query("cid") id: String
    ): ApiResult<PageVO<ArticleListVO>>

    /**
     * 获取导航数据
     */
    @GET("/navi/json")
    suspend fun getNavigationList(): ApiResult<List<NavigationVO>>


    /**
     * 获取文章列表
     */
    @GET("/wenda/list/{pageNum}/json")
    suspend fun getAskArticleListByPage(@Path("pageNum") pageNum: Int): ApiResult<PageVO<ArticleListVO>>

    /**
     * 获取项目分类
     */
    @GET("/project/tree/json")
    suspend fun getProjectCategoriesList(): ApiResult<List<CategoryVO>>

    /**
     * 获取项目列表
     */
    @GET("/project/list/{page}/json")
    suspend fun getProjectArticleListByPage(
        @Path("page") pageNum: Int,
        @Query("cid") cid: Int
    ): ApiResult<PageVO<ArticleListVO>>

    /**
     * 获取热门搜索
     */
    @GET("hotkey/json")
    suspend fun getSearchHot(): ApiResult<List<SearchHotVO>>

    /**
     * 根据关键词搜索数据
     */
    @POST("article/query/{page}/json")
    suspend fun getSearchDataByKey(
        @Path("page") pageNo: Int,
        @Query("k") searchKey: String
    ): ApiResult<PageVO<ArticleListVO>>


    /**
     * 获取微信公众号分类
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getWechatCategoriesList(): ApiResult<List<CategoryVO>>

    /**
     * 获取微信公众号列表
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getWechatArticleListByPage(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): ApiResult<PageVO<ArticleListVO>>

    /**
     * 登录（表单POST）
     * @param account 账号
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") account: String,
        @Field("password") password: String
    ): ApiResult<UserInfoVO>

}