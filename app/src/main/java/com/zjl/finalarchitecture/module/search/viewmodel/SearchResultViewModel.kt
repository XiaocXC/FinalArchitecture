package com.zjl.finalarchitecture.module.search.viewmodel

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
import com.zjl.finalarchitecture.data.respository.datasouce.SearchResultPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-05-03
 **/
class SearchResultViewModel(
    savedStateHandle: SavedStateHandle
): PagingBaseViewModel() {

    private val key: String = savedStateHandle["key"] ?: ""

    private val name: String = savedStateHandle["name"] ?: ""

    private val _title = MutableStateFlow(key)
    val title: StateFlow<String> get() = _title

    private val _searchResults = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val searchResults: StateFlow<PagingUiModel<ArticleListVO>> = _searchResults

    init {
        initData()
    }

    override fun loadMoreInner(currentIndex: Int) {
        viewModelScope.launch {
            // 先置为加载中
            if(currentIndex == initPageIndex()){
                _searchResults.value = PagingUiModel.Loading(true)
            }

            launchRequestByPaging({
                ApiRepository.requestSearchDataByKey(currentIndex, key)
            }, successBlock = {
                _searchResults.value = PagingUiModel.Success(it.dataList, currentIndex == initPageIndex(), !it.over)
            }, failureBlock = {
                _searchResults.value = PagingUiModel.Error(ApiException(it), currentIndex == initPageIndex())
            })
        }
    }
}