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

//    val coldFlow = flow {
//        emit(1)
//        emit(2)
//        emit(2)
//        emit(2)
//        emit(2)
//        emit(2)
//        emit(2)
//    }.flowOn(Dispatchers.IO) // 以上内容在IO线程执行
//        .map {
//
//        }.map {
//
//        }.flowOn(Dispatchers.Default)  // 以上Map内容在Default线程执行
//
//    val hotFlow = MutableStateFlow(2)
//
//    fun test(){
//        viewModelScope.launch {
//            coldFlow.collect {
//                // 打印
//                // 1  2
//            }
//
//            coldFlow.collect {
//                // 打印
//                // 1  2
//            }
//
//            hotFlow.collect {
//                // 打印
//                // 2
//
//                // 执行下面的内容后
//                // 5
//            }
//
//            hotFlow.value = 5
//        }
//    }

//    private val _plazaListFlow = MutableStateFlow<MutableList<ArticleListVO>>(mutableListOf())
//    val mPlazaListFlow: StateFlow<MutableList<ArticleListVO>> = _plazaListFlow
//
//    // BannerUI
//    private val _addPlazaListFlow = MutableStateFlow<List<ArticleListVO>>(emptyList())
//    val mAddPlazaListFlow: StateFlow<List<ArticleListVO>> = _addPlazaListFlow
//
//    private var currentPage = 0

    private val _plazaList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val plazaList: StateFlow<PagingUiModel<ArticleListVO>> = _plazaList

    init {
        initData()
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