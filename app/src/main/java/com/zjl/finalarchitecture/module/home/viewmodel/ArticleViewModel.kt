package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.ui.UiModel
import com.zjl.base.ui.data
import com.zjl.base.ui.onSuccess
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.model.PageVO
import com.zjl.finalarchitecture.module.home.repository.HomeRepository
import com.zjl.finalarchitecture.module.home.repository.IntegerPagingSource
import com.zjl.library_network.ApiResult
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleViewModel : BaseViewModel() {

    //页码 首页数据页码从0开始
    var pageNo = 0

    // BannerUI状态壳
    private val _bannerListUiModel = MutableLiveData<UiModel<List<BannerVO>>>()
    val bannerListUiModel: LiveData<UiModel<List<BannerVO>>> get() = _bannerListUiModel

    // ArticleDataUI状态壳
    private val _articleListUiModel = MutableLiveData<UiModel<PageVO<ArticleListVO>>>()
    val mArticleListUiModel: LiveData<UiModel<PageVO<ArticleListVO>>> get() = _articleListUiModel


    val articlePagingFlow = Pager(PagingConfig(pageSize = 20)) {
        object : IntegerPagingSource<ArticleListVO>() {
            override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
                return HomeRepository.requestArticleByPage(currentPage)
            }
        }
    }.flow.cachedIn(viewModelScope)

    init {

        toReFresh()
    }

    /**
     * 刷新
     */
    fun toReFresh() {
        responseBanner()
//        refreshArticle()
    }

    private fun responseBanner() {
        // 获取Banner图列表
        viewModelScope.launch {
            // 先给个Loading状态
            _bannerListUiModel.value = UiModel.Loading()
            // 请求数据
            val bannerResult = HomeRepository.requestBanner()
            _bannerListUiModel.value = bannerResult
        }
    }

    private fun refreshArticle() {
        viewModelScope.launch {
            // TODO: 2022/2/8
            val topDataDeferred = viewModelScope.async {
                HomeRepository.requestTopArticleData()
            }
            val articleDataDeferred = viewModelScope.async {
                HomeRepository.requestArticleByPageData(pageNo)
            }
            val topArticleList = topDataDeferred.await()
            val pagination = articleDataDeferred.await()
        }
    }
}