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
import com.zjl.finalarchitecture.module.home.model.PageVO
import com.zjl.finalarchitecture.module.home.repository.HomeRepository
import com.zjl.finalarchitecture.module.home.repository.IntegerPagingSource
import com.zjl.library_network.ApiResult
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleViewModel: BaseViewModel() {

    // BannerUI状态壳
    private val _bannerListUiModel = MutableLiveData<UiModel<List<BannerVO>>>()
    val bannerListUiModel: LiveData<UiModel<List<BannerVO>>> get() = _bannerListUiModel

    val articlePagingFlow = Pager(
        PagingConfig(pageSize = 20)
    ){
        object: IntegerPagingSource<ArticleListVO>(){
            override suspend fun loadData(currentPage: Int): ApiResult<PageVO<ArticleListVO>> {
                return HomeRepository.getArticleByPage(currentPage)
            }
        }
    }.flow
        .cachedIn(viewModelScope)

    init {
        refresh()
    }

    /**
     * 刷新
     */
    fun refresh(){
        refreshBanner()
        refreshArticle()
    }

    private fun refreshBanner(){
        // 获取Banner图列表
        viewModelScope.launch {
            // 先给个Loading状态
            _bannerListUiModel.value = UiModel.Loading()
            // 请求数据
            val bannerResult = HomeRepository.getBanner()
            _bannerListUiModel.value = bannerResult
        }
    }

    private fun refreshArticle(){
        viewModelScope.launch {

        }
    }
}