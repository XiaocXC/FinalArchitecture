package com.zjl.finalarchitecture.module.home.viewmodel

import com.zjl.base.ui.PagingUiModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.utils.ext.paging.requestPagingApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlazaViewModel: PagingBaseViewModel()  {

    private val _plazaList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val plazaList: StateFlow<PagingUiModel<ArticleListVO>> = _plazaList

    private var mCurrentIndex = initPageIndex()

    init {
        // 初始化时请求刷新数据
        onRefreshData()
    }

    override fun onRefreshData(tag: Any?) {
        mCurrentIndex = initPageIndex()
        loadMorePlaza(mCurrentIndex)
    }

    override fun onLoadMoreData(tag: Any?) {
        ++ mCurrentIndex
        loadMorePlaza(mCurrentIndex)
    }

    fun loadMorePlaza(currentIndex: Int) {
        requestScope {
            requestPagingApiResult(isRefresh = currentIndex == initPageIndex(), pagingUiModel = _plazaList){
                ApiRepository.requestPlazaArticleDataByPage(currentIndex)
            }.await()
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