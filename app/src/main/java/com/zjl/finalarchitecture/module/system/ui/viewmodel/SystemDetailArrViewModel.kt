package com.zjl.finalarchitecture.module.system.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.model.system.ClassifyVO
import com.zjl.finalarchitecture.module.home.model.system.SystemVO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SystemDetailArrViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private val systemData: SystemVO? = savedStateHandle["detailData"]
    private val systemNavIndex: Int = savedStateHandle["index"] ?: 0

    private val _systemChild = MutableStateFlow<Pair<Int, List<ClassifyVO>>>(Pair(systemNavIndex, emptyList()))
    val systemChild: StateFlow<Pair<Int, List<ClassifyVO>>> = _systemChild

    init {
        toRefresh()
    }

    override fun refresh() {
        val data = systemData ?: return
        _systemChild.value = Pair(systemNavIndex, data.children)
    }

}