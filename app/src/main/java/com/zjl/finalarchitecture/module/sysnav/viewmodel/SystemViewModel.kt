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
    private val _systemList = MutableStateFlow<UiModel<List<SystemVO>>>(UiModel.Loading())
    val systemList: StateFlow<UiModel<List<SystemVO>>> = _systemList

    init {
        initData()
    }

    /**
     * 加载体系数据
     */
    private fun loadSystemData(){
        viewModelScope.launch {
            launchRequestByNormalWithUiState(requestAction = {
                ApiRepository.requestSystemListData()
            }, resultState = _systemList, isShowLoading = true)
        }
    }

    override fun initData() {
        loadSystemData()
    }
}