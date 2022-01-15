package com.zjl.finalarchitecture.module.main.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.zjl.base.globalApplication
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.module_domain.UiModel
import com.zjl.module_domain.data
import com.zjl.module_domain.model.banner.BannerVO
import com.zjl.module_domain.repository.HomeRepository
import com.zjl.module_domain.usecase.banner.HomeBannerUseCase
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-01-07
 */
class MainViewModel : BaseViewModel() {

    // BannerUI状态壳
    private val _bannerListUiModel = MutableLiveData<UiModel<List<BannerVO>>>()
    val bannerListUiModel: LiveData<UiModel<List<BannerVO>>> get() = _bannerListUiModel

    // BannerUI数据
    val bannerList = _bannerListUiModel.switchMap {
        liveData {
            if(it is UiModel.Success){
                emit(it.data)
            }
        }
    }

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
            val bannerResult = HomeRepository.getBanner()
            _bannerListUiModel.value = bannerResult
        }
    }

}