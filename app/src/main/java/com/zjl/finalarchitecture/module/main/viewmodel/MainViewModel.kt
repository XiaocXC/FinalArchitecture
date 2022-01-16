package com.zjl.finalarchitecture.module.main.viewmodel

import androidx.lifecycle.*
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.module_domain.model.banner.BannerVO
import com.zjl.module_domain.repository.HomeRepository
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-01-07
 */
class MainViewModel : BaseViewModel() {

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