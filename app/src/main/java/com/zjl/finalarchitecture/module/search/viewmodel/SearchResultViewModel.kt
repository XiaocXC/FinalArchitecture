package com.zjl.finalarchitecture.module.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.respository.datasouce.SearchResultPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Xiaoc
 * @since  2022-05-03
 **/
class SearchResultViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private val key: String = savedStateHandle["key"] ?: ""

    private val name: String = savedStateHandle["name"] ?: ""

    private val _title = MutableStateFlow(key)
    val title: StateFlow<String> get() = _title

    // 文章
    val searchResultPagingFlow = Pager(PagingConfig(pageSize = 20)) {
        SearchResultPagingSource(name)
    }.flow.cachedIn(viewModelScope)

    init {
        initData()
    }

    override fun refresh() {

    }
}