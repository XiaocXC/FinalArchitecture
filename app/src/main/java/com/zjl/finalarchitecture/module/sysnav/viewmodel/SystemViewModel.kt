package com.zjl.finalarchitecture.module.sysnav.viewmodel

import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.SystemVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
        loadSystemData()
    }

    /**
     * 加载体系数据
     */
    private fun loadSystemData(){
        requestScope {
            requestApiResult(uiModel = _systemList) {
                ApiRepository.requestSystemListData()
            }.await()
        }
    }
}