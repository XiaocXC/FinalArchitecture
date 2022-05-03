package com.zjl.finalarchitecture.module.sysnav.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.model.ClassifyVO
import com.zjl.finalarchitecture.data.model.SystemVO

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SystemDetailArrViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    /**
     * 子栏目的List数据，通过[savedStateHandle]获取从上一个Fragment传来
     */
    private val systemData: SystemVO? = savedStateHandle["detailData"]

    /**
     * 默认选中的子栏目Index，通过[savedStateHandle]获取从上一个Fragment传来
     */
    private val systemNavIndex: Int = savedStateHandle["index"] ?: 0

    private val _systemChild = MutableStateFlow<Pair<Int, List<ClassifyVO>>>(Pair(systemNavIndex, emptyList()))
    val systemChild: StateFlow<Pair<Int, List<ClassifyVO>>> = _systemChild

    init {
        initData()
    }

    override fun refresh() {
        val data = systemData ?: return
        _systemChild.value = Pair(systemNavIndex, data.children)
    }

}