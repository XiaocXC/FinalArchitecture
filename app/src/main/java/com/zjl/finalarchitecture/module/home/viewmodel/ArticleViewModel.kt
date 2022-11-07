package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.zjl.base.exception.ApiException
import com.zjl.base.onSuccess
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.ui.UiModel
import com.zjl.base.ui.data
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.BannerVO
import com.zjl.finalarchitecture.data.model.PageVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.utils.CacheUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

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

    init {
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
     * 加载分页文章数据
     * @param currentIndex 当前加载的分页数据
     */
    private fun requestArticle(currentIndex: Int){
        viewModelScope.launch {
            // 先置为加载中
            if(currentIndex == initPageIndex()){
                _articleList.value = PagingUiModel.Loading(true)
            }

            launchRequestByNormal({
                // 如果App配置打开了首页请求置顶文章(为什么做这个，我们可以再设置页面灵活开关)，且是第一页，则额外请求一个置顶文章列表
                val articlePageData = ApiRepository.requestArticleDataByPage(currentIndex)
                if (CacheUtil.isNeedTop() && currentIndex == initPageIndex()) {
                    val topArticleList = mutableListOf<ArticleListVO>()

                    val topArticleListData = ApiRepository.requestTopArticleData()
                    topArticleListData.onSuccess {
                        topArticleList.addAll(it)
                    }
                    articlePageData.onSuccess {
                        it.dataList.addAll(0, topArticleList)
                    }
                }
                articlePageData
            }, successBlock = {
                _articleList.value = PagingUiModel.Success(it.dataList, currentIndex == initPageIndex(), it.over)
            }, failureBlock = {
                _articleList.value = PagingUiModel.Error(ApiException(it), currentIndex == initPageIndex())
            })
        }
    }

    /**
     * 刷新Banner数据
     */
    private fun requestBanner() {
        viewModelScope.launch {
            launchRequestByNormal({
                ApiRepository.requestBanner()
            }, successBlock = {
                _bannerList.value = it
            })
        }
    }


}