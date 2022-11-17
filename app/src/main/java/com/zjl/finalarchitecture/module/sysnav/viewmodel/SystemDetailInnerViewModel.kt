package com.zjl.finalarchitecture.module.sysnav.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.utils.ext.paging.requestPagingApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 体系每个子栏目ViewModel
 */
class SystemDetailInnerViewModel(
    savedStateHandle: SavedStateHandle
): PagingBaseViewModel() {

    /**
     * 子栏目的Id，通过[savedStateHandle]获取从上一个Fragment传来的id值
     */
    private val id: String = savedStateHandle["id"] ?: ""

    private val _systemArticleList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val systemArticleList: StateFlow<PagingUiModel<ArticleListVO>> = _systemArticleList

    private var mCurrentIndex: Int = initPageIndex()

    init {
        onRefreshData()
    }

    fun loadMoreSystemDetailList(currentIndex: Int) {
        viewModelScope.launch {
            requestScope {
                requestPagingApiResult(isRefresh = currentIndex == initPageIndex(), pagingUiModel = _systemArticleList){
                    ApiRepository.requestSystemListData(currentIndex, id)
                }.await()
            }
        }
    }

    override fun onRefreshData(tag: Any?) {
        mCurrentIndex = initPageIndex()
        loadMoreSystemDetailList(mCurrentIndex)
    }

    override fun onLoadMoreData(tag: Any?) {
        mCurrentIndex ++
        loadMoreSystemDetailList(mCurrentIndex)
    }
}