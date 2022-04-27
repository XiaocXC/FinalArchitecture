package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.model.CategoryVO
import com.zjl.finalarchitecture.data.model.SystemVO
import com.zjl.finalarchitecture.data.respository.datasouce.AskPagingSource
import com.zjl.finalarchitecture.data.respository.datasouce.PlazaPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @description:
 * @author: zhou
 * @date : 2022/4/25 19:46
 */
class ProjectViewModel : BaseViewModel() {

    /**
     * 项目列表数据
     */
    private val _categoryList = MutableStateFlow<List<CategoryVO>>(emptyList())
    val categoryList: StateFlow<List<CategoryVO>> = _categoryList

    override fun refresh() {

    }
}