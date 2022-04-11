package com.zjl.finalarchitecture.module.sysnav.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.respository.datasouce.SystemArticlePagingSource

/**
 * 体系每个子栏目ViewModel
 */
class SystemDetailInnerViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    /**
     * 子栏目的Id，通过[savedStateHandle]获取从上一个Fragment传来的id值
     */
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