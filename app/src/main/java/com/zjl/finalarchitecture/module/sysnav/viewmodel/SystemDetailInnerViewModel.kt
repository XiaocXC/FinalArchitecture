package com.zjl.finalarchitecture.module.sysnav.viewmodel

import androidx.lifecycle.SavedStateHandle
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
import com.zjl.finalarchitecture.data.respository.datasouce.SystemArticlePagingSource
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

    init {
        initData()
    }

    override fun loadMoreInner(currentIndex: Int) {
        viewModelScope.launch {
            // 先置为加载中
            if(currentIndex == initPageIndex()){
                _systemArticleList.value = PagingUiModel.Loading(true)
            }

            launchRequestByPaging({
                ApiRepository.requestSystemListData(currentIndex, id)
            }, successBlock = {
                _systemArticleList.value = PagingUiModel.Success(it.dataList, currentIndex == initPageIndex(), !it.over)
            }, failureBlock = {
                _systemArticleList.value = PagingUiModel.Error(ApiException(it), currentIndex == initPageIndex())
            })
        }
    }
}