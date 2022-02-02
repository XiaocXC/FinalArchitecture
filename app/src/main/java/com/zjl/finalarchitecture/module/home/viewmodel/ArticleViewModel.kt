package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.repository.HomeRepository
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleViewModel: BaseViewModel() {

    // BannerUI状态壳
    private val _bannerListUiModel = MutableLiveData<UiModel<List<BannerVO>>>()
    val bannerListUiModel: LiveData<UiModel<List<BannerVO>>> get() = _bannerListUiModel

    init {
        refresh()
    }

    /**
     * 刷新
     */
    fun refresh(){
        refreshBanner()
//        refreshArticle()
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
}