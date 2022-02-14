package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.repository.HomeRepository
import com.zjl.base.exception.ApiException
import com.zjl.finalarchitecture.module.home.repository.ArticlePagingSource
import timber.log.Timber

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleViewModel : BaseViewModel() {

    // BannerUI
    private val _bannerListUiModel = MutableLiveData<List<BannerVO>>()
    val bannerListUiModel: LiveData<List<BannerVO>> get() = _bannerListUiModel

    val articlePagingFlow = Pager(PagingConfig(pageSize = 20)) {
        ArticlePagingSource()
    }.flow
        .cachedIn(viewModelScope)

    init {
        toRefresh()
    }

    override fun refresh() {
        // 请求中状态
        refreshBanner()
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
}