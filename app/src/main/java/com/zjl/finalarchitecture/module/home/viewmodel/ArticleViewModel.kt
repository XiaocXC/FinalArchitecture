package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.repository.HomeRepository
import com.zjl.base.exception.ApiException
import timber.log.Timber

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleViewModel : BaseViewModel() {

    //页码 首页数据页码从0开始
    var pageNo = 0

    // BannerUI
    private val _bannerListUiModel = MutableLiveData<List<BannerVO>>()
    val bannerListUiModel: LiveData<List<BannerVO>> get() = _bannerListUiModel

    // ArticleDataUI
    private val _articleListUiModel = MutableLiveData<MutableList<ArticleListVO>>()
    val articleListUiModel: LiveData<MutableList<ArticleListVO>> get() = _articleListUiModel

    private val _addArticleList = MutableLiveData<List<ArticleListVO>>()
    val addArticleList: LiveData<List<ArticleListVO>> get() = _addArticleList

    init {
        toReFresh()
    }

    /**
     * 刷新
     */
    fun toReFresh() {
        //重置为0
        pageNo = 0
        // 请求中状态
        _rootViewState.tryEmit(UiModel.Loading())
        refreshBanner()
        refreshArticle()
    }

    /**
     * 刷新Banner数据
     */
    private fun refreshBanner(){
        launchRequestByNormal({
            HomeRepository.requestBanner()
        }, successBlock = {
            _bannerListUiModel.value = it
        })
    }

    /**
     * 刷新文章数据
     */
    private fun refreshArticle(){
        launchRequestByNormal({
            HomeRepository.requestArticleByPageData(pageNo)
        }, successBlock = {
            // 成功状态
            _rootViewState.emit(UiModel.Success(Unit))
            pageNo = it.currentPage
            Timber.tag("zjl").v("当前页码：%s", pageNo)
            _articleListUiModel.value = it.dataList
        }, failureBlock = {
            // 失败状态
            _rootViewState.emit(UiModel.Error(ApiException(it)))
        })
    }

    /**
     * 加载更多
     * 我感觉有点多余，但我不知道咋写了
     * 可以和上面请求合并
     * 而且还有一个问题
     * 数据先加载出来后，加载更多的布局才消失
     */
    fun loadMoreArticle(){
        launchRequestByNormal({
            HomeRepository.requestArticleByPageData(pageNo)
        }, successBlock = {
            pageNo = it.currentPage
            Timber.tag("zjl").v("当前页码：%s", pageNo)
            _addArticleList.value = it.dataList
            _addArticleList.value = emptyList()

            // 与原来的数据叠加
            _articleListUiModel.value?.addAll(it.dataList)
        })
    }
}