package com.zjl.finalarchitecture.module.home.viewmodel

import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.repository.resp.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlazaViewModel: BaseViewModel()  {

    // BannerUI
    private val _plazaListFlow = MutableStateFlow<List<ArticleListVO>>(emptyList())
    val mPlazaListFlow: StateFlow<List<ArticleListVO>> = _plazaListFlow

    companion object {
        const val INITIAL_PAGE = 0
    }

    /**
     * 获取广场文章
     */
    fun requestPlazaData(){
        launchRequestByNormal({
            HomeRepository.requestPlazaArticleData(INITIAL_PAGE)
        }, successBlock = {
//            _bannerListUiModel.value = it
            _plazaListFlow.value = it.dataList
        })
    }


    override fun refresh() {

    }
}