package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.respository.datasouce.AskPagingSource
import com.zjl.finalarchitecture.data.respository.datasouce.PlazaPagingSource

/**
 * @description:
 * @author: zhou
 * @date : 2022/4/25 19:46
 */
class AskViewModel : BaseViewModel() {

    // 问答文章
    val askPagingFlow = Pager(
        PagingConfig(pageSize = 20)
    ) {
        AskPagingSource()
    }.flow.cachedIn(viewModelScope)

    override fun refresh() {

    }
}