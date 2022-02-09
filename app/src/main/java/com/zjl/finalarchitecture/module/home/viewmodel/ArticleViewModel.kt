package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.repository.HomeRepository
import com.zjl.base.exception.ApiException

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
    private val _articleListUiModel = MutableLiveData<List<ArticleListVO>>()
    val articleListUiModel: LiveData<List<ArticleListVO>> get() = _articleListUiModel

    init {
        pageNo = 0
        toReFresh()
    }

    /**
     * 刷新
     */
    fun toReFresh() {
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

            _articleListUiModel.value = it.dataList
        }, failureBlock = {
            // 失败状态
            _rootViewState.emit(UiModel.Error(ApiException(it)))
        })
    }
}