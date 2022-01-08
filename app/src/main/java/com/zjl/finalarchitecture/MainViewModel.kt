package com.zjl.finalarchitecture

import android.app.Application
import androidx.lifecycle.*
import com.zjl.base.globalApplication
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.module_domain.UiModel
import com.zjl.module_domain.model.banner.BannerVO
import com.zjl.module_domain.usecase.banner.HomeBannerUseCase
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-01-07
 */
class MainViewModel(application: Application = globalApplication): BaseViewModel(application) {

    private val _bannerListUiModel = MutableLiveData<UiModel<List<BannerVO>>>()
    val bannerListUiModel: LiveData<UiModel<List<BannerVO>>> get() = _bannerListUiModel

    val bannerList = _bannerListUiModel.switchMap {
        liveData {
            if(it is UiModel.Success){
                emit(it)
            }
        }
    }

    private val homeBannerUseCase = HomeBannerUseCase()

    init {
        viewModelScope.launch {
            val result = homeBannerUseCase(Unit)
            _bannerListUiModel.value = result
        }
    }
}