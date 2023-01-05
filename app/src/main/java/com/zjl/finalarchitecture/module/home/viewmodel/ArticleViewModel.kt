package com.zjl.finalarchitecture.module.home.viewmodel

import com.zjl.base.map
import com.zjl.base.onSuccess
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.BannerVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.utils.CacheUtil
import com.zjl.finalarchitecture.utils.ext.paging.requestPagingApiResult
import kotlinx.coroutines.flow.*

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleViewModel : PagingBaseViewModel() {

    // Banner数据
    private val _bannerList = MutableStateFlow<List<BannerVO>>(emptyList())
    val bannerList: StateFlow<List<BannerVO>> = _bannerList

    // 文章列表
    private val _articleList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val articleList: StateFlow<PagingUiModel<ArticleListVO>> = _articleList

    private var mCurrentIndex: Int = initPageIndex()

    private val _collectArticleEvent = MutableSharedFlow<UiModel<Int>>()
    val collectArticleEvent: SharedFlow<UiModel<Int>> = _collectArticleEvent

    init {
        // 初始化时请求刷新数据
        onRefreshData()
    }

    override fun pageSize(): Int {
        return super.pageSize()
    }

    override fun onRefreshData(tag: Any?) {
        mCurrentIndex = initPageIndex()
        requestBanner()
        requestArticle(mCurrentIndex)
    }

    override fun onLoadMoreData(tag: Any?) {
        ++mCurrentIndex
        requestArticle(mCurrentIndex)
    }

    /**
     * 收藏文章
     */
    fun collectArticle(id: Int){
        requestScope {
            try {
                requestApiResult {
                    ApiRepository.requestCollectArticle(id).map {
                        // 从Unit的返回数据转回为Id存的数据
                        id
                    }
                }.await()

                // 更新数据源
                // 找到对应Id的索引
                val changedItemIndex = _articleList.value.totalList.indexOfFirst {
                    it.id == id
                }
                if(changedItemIndex >= 0){
                    // 更新收藏信息
                    val changedItem = _articleList.value.totalList[changedItemIndex]
                    changedItem.collect = true
                    _collectArticleEvent.emit(UiModel.Success(changedItemIndex))
                }

            } catch (e: Exception){
                e.printStackTrace()
            }

        }
    }


    /**
     * 取消收藏文章
     */
    fun unCollectArticle(id: Int){
        requestScope {
            try {
                requestApiResult {
                    ApiRepository.requestUnCollectArticle(id)
                }.await()

                // 更新数据源
                // 找到对应Id的索引
                val changedItemIndex = _articleList.value.totalList.indexOfFirst {
                    it.id == id
                }
                if(changedItemIndex >= 0){
                    // 更新收藏信息
                    val changedItem = _articleList.value.totalList[changedItemIndex]
                    changedItem.collect = false
                    _collectArticleEvent.emit(UiModel.Success(changedItemIndex))
                }

            } catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    /**
     * 加载分页文章数据
     * @param currentIndex 当前加载的分页数据
     */
    private fun requestArticle(currentIndex: Int){
        // 启动请求协程域，里面的任何错误均可被处理
        requestScope {
            // 调用快捷的分页请求方法，具体使用请见该方法注释
            requestPagingApiResult(isRefresh = currentIndex == initPageIndex(), pagingUiModel = _articleList){
                // 1.获取文章分页数据
                val articlePageData = ApiRepository.requestArticleDataByPage(currentIndex)
                // 2.当我们开启了置顶显示且是第一页时，我们请求一次置顶数据
                if (CacheUtil.isNeedTop() && currentIndex == initPageIndex()) {
                    val topArticleList = mutableListOf<ArticleListVO>()

                    // 3.获取置顶数据，如果成功，我们将其组装到总数据中
                    val topArticleListData = ApiRepository.requestTopArticleData()
                    topArticleListData.onSuccess {
                        topArticleList.addAll(it)
                    }
                    articlePageData.onSuccess {
                        it.dataList.addAll(0, topArticleList)
                    }
                }
                articlePageData
            }.await()
        }
    }

    /**
     * 刷新Banner数据
     */
    private fun requestBanner() {
        requestScope {
            // 调用网络请求方法，具体使用请见该方法注释
            val data = requestApiResult {
                ApiRepository.requestBanner()
            }.await()
            _bannerList.value = data
        }
    }


}