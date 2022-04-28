package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.api.ApiService
import com.zjl.finalarchitecture.data.model.CategoryVO
import com.zjl.finalarchitecture.data.model.SystemVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.AskPagingSource
import com.zjl.finalarchitecture.data.respository.datasouce.PlazaPagingSource
import com.zjl.finalarchitecture.data.respository.datasouce.ProjectArticlePagingSource
import com.zjl.finalarchitecture.data.respository.datasouce.SystemArticlePagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @description:
 * @author: zhou
 * @date : 2022/4/25 19:46
 */
class ProjectViewModel() : BaseViewModel() {

    /**
     * 点击id
     */
    var cid = 0

    /**
     * 项目分类列表数据
     */
    private val _categoryList = MutableStateFlow<List<CategoryVO>>(emptyList())
    val categoryList: StateFlow<List<CategoryVO>> = _categoryList


    /**
     * 项目列表数据
     */
    val projectArticlePagingFlow = Pager(PagingConfig(pageSize = 20)) {
        ProjectArticlePagingSource(cid)
    }.flow.cachedIn(viewModelScope)

    init {
        requestCategory()
    }

    override fun refresh() {

    }

    /**
     * 请求项目分类
     */
    private fun requestCategory() {
        viewModelScope.launch {
            launchRequestByNormal({
                ApiRepository.requestProjectListData()
            }, successBlock = { data ->
                // 状态更改为成功
                _rootViewState.emit(UiModel.Success(data))
                _categoryList.value = data
            },failureBlock = { error ->
                // 状态更改为错误
                _rootViewState.emit(UiModel.Error(ApiException(error)))
            })
        }
    }

}