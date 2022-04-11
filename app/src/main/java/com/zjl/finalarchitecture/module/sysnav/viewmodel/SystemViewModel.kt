package com.zjl.finalarchitecture.module.sysnav.viewmodel

import androidx.lifecycle.viewModelScope
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.model.SystemVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 体系ViewModel
 */
class SystemViewModel: BaseViewModel() {

    /**
     * 体系列表数据
     */
    private val _systemList = MutableStateFlow<List<SystemVO>>(emptyList())
    val systemList: StateFlow<List<SystemVO>> = _systemList

    init {
        toRefresh()
    }

    /**
     * 加载体系数据
     */
    private fun loadSystemData(){
        viewModelScope.launch {
            launchRequestByNormal({
                ApiRepository.requestSystemListData()
            },{ data ->
                // 状态更改为成功
                _rootViewState.emit(UiModel.Success(data))
                _systemList.value = data
            },{ error ->
                // 状态更改为错误
                _rootViewState.emit(UiModel.Error(ApiException(error)))
            })
        }
    }

    override fun refresh() {
        loadSystemData()
    }
}