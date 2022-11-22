package com.zjl.finalarchitecture.data.respository

import com.zjl.base.ApiResult
import com.zjl.base.onSuccess
import com.zjl.finalarchitecture.utils.CacheUtil
import com.zjl.finalarchitecture.api.ApiService
import com.zjl.finalarchitecture.api.mWanAndroidRetrofit
import com.zjl.finalarchitecture.data.model.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * @author Xiaoc
 * @since 2022-01-08
 *
 * 仓库层
 * 方法命名必须以 request 开头
 */
object ApiRepository {

    private val mApiService by lazy { mWanAndroidRetrofit.create(ApiService::class.java) }

    /**
     * 获取Banner数据
     * @return Banner列表
     */
    suspend fun requestBanner(): ApiResult<List<BannerVO>> {
        return mApiService.getBanner()
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
                mApiService.getArticleListByPage(currentPage)
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
        return mApiService.getTopArticleList()
    }

    /**
     * 分页获取文章
     * @param pageNo 当前页码
     */
    suspend fun requestArticleDataByPage(pageNo: Int): ApiResult<PageVO<ArticleListVO>> {
        return mApiService.getArticleListByPage(pageNo)
    }


    /**
     * 分页获取广场文章
     * @param pageNo 当前页码
     */
    suspend fun requestPlazaArticleDataByPage(pageNo: Int): ApiResult<PageVO<ArticleListVO>> {
        return mApiService.getPlazaArticleListByPage(pageNo)
    }

    /**
     * 获取体系数据
     */
    suspend fun requestSystemListData(): ApiResult<List<SystemVO>> {
        return mApiService.getSystemList()
    }

    /**
     * 获取体系子栏目列表数据
     * @param id 栏目ID
     * @param pageNo 页码
     */
    suspend fun requestSystemListData(pageNo: Int, id: String): ApiResult<PageVO<ArticleListVO>> {
        return mApiService.getSystemChildData(pageNo, id)
    }

    /**
     * 获取导航数据
     */
    suspend fun requestNavigationListData(): ApiResult<List<NavigationVO>> {
        return mApiService.getNavigationList()
    }

    /**
     * 获取每日一问数据
     */
    suspend fun requestAskArticleListDataByPage(pageNo: Int): ApiResult<PageVO<ArticleListVO>> {
        return mApiService.getAskArticleListByPage(pageNo)
    }

    /**
     * 获取项目分类
     */
    suspend fun requestProjectListData(): ApiResult<List<CategoryVO>> {
        return mApiService.getProjectCategoriesList()
    }

    /**
     * 获取项目详情列表
     * @param pageNo 页码
     * @param cid 栏目ID
     */
    suspend fun requestProjectDetailListDataByPage(
        pageNo: Int,
        cid: Int
    ): ApiResult<PageVO<ArticleListVO>> {
        return mApiService.getProjectArticleListByPage(pageNo, cid)
    }

    /**
     * 获取微信公众号分类
     */
    suspend fun requestWechatListData(): ApiResult<List<CategoryVO>> {
        return mApiService.getWechatCategoriesList()
    }

    /**
     * 获取微信公众号详情列表
     * @param pageNo 页码
     * @param id 栏目ID
     */
    suspend fun requestWechatDetailListDataByPage(
        id: Int,
        page: Int
    ): ApiResult<PageVO<ArticleListVO>> {
        return mApiService.getWechatArticleListByPage(id, page)
    }

    /**
     * 获取热门搜索
     */
    suspend fun requestSearchHot(): ApiResult<List<SearchHotVO>> {
        return mApiService.getSearchHot()
    }

    /**
     * 根据关键词搜索数据
     */
    suspend fun requestSearchDataByKey(page: Int, key: String): ApiResult<PageVO<ArticleListVO>> {
        return mApiService.getSearchDataByKey(page, key)
    }

    /**
     * 登录账户
     * @param account 账号
     * @param password 密码
     */
    suspend fun requestLogin(account: String, password: String): ApiResult<UserInfoVO> {
        return mApiService.login(account, password)
    }

    /**
     * 注册账户
     * @param account 账号
     * @param password 密码
     */
    suspend fun requestRegister(account: String, password: String): ApiResult<Unit> {
        return mApiService.register(account, password, password)
    }

    /**
     * 退出账户
     */
    suspend fun requestLogout(): ApiResult<Unit> {
        return mApiService.logout()
    }


    /**
     * 教程
     */
    suspend fun requestTutorialListData(): ApiResult<List<TutorialVO>> {
        return mApiService.getTutorialList()
    }

    /**
     * 教程详情
     */
    suspend fun requestTutorialDetailListData(
        page: Int,
        cid: Int?,
        orderType: Int = 1
    ): ApiResult<PageVO<ArticleListVO>> {
        return mApiService.getTutorialDetailList(page, cid, orderType)
    }
}