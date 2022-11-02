package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.PlazaPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlazaViewModel: PagingBaseViewModel()  {



    private val _plazaList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val plazaList: StateFlow<PagingUiModel<ArticleListVO>> = _plazaList

    init {
        onRefreshData()
    }

    override fun onRefreshData(tag: Any?) {
        TODO("Not yet implemented")
    }

    override fun onLoadMoreData(tag: Any?) {
        TODO("Not yet implemented")
    }

    override fun loadMoreInner(currentIndex: Int) {
        viewModelScope.launch {
            // 先置为加载中
            if(currentIndex == initPageIndex()){
                _plazaList.value = PagingUiModel.Loading(true)
            }

            launchRequestByPaging({
                ApiRepository.requestPlazaArticleDataByPage(currentIndex)
            }, successBlock = {
                _plazaList.value = PagingUiModel.Success(it.dataList, currentIndex == initPageIndex(), !it.over)
            }, failureBlock = {
                _plazaList.value = PagingUiModel.Error(ApiException(it), currentIndex == initPageIndex())
            })
        }
    }

//    /**
//     * 获取广场文章
//     */
//    fun requestPlazaData(){
//        viewModelScope.launch {
//            launchRequestByNormal({
//                ApiRepository.requestPlazaArticleDataByPage(currentPage)
//            }, successBlock = {
////            _bannerListUiModel.value = it
//                _addPlazaListFlow.value = it.dataList
//                _plazaListFlow.value.addAll(it.dataList)
//                _addPlazaListFlow.value = mutableListOf()
//
//                currentPage = it.currentPage
//            })
//        }
//    }

}