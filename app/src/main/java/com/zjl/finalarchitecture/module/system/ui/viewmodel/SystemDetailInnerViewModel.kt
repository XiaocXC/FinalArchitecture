package com.zjl.finalarchitecture.module.system.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.repository.datasouce.SystemArticlePagingSource

class SystemDetailInnerViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private val id: String = savedStateHandle["id"] ?: ""

    // 体系子栏目文章
    val systemArticlePagingFlow = Pager(PagingConfig(pageSize = 20)) {
        SystemArticlePagingSource(id)
    }.flow.cachedIn(viewModelScope)

    init {
        toRefresh()
    }

    override fun refresh() {}
}