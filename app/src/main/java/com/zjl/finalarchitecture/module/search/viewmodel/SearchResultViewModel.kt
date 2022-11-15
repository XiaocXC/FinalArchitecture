package com.zjl.finalarchitecture.module.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.utils.ext.paging.requestPagingApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

    private var mCurrentIndex: Int = initPageIndex()

    init {
        // 初始化时加载数据
        onRefreshData()
    }

    fun loadMoreInner(currentIndex: Int) {
        requestScope {
            requestPagingApiResult(isRefresh = currentIndex == initPageIndex(), pagingUiModel = _searchResults) {
                ApiRepository.requestSearchDataByKey(currentIndex, key)
            }.await()
        }
    }

    override fun onRefreshData(tag: Any?) {
        mCurrentIndex = initPageIndex()
        loadMoreInner(mCurrentIndex)
    }

    override fun onLoadMoreData(tag: Any?) {
        ++mCurrentIndex
        loadMoreInner(mCurrentIndex)
    }
}