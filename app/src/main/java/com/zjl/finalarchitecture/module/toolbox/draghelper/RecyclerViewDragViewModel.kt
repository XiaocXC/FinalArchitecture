package com.zjl.finalarchitecture.module.toolbox.draghelper

import androidx.lifecycle.viewModelScope
import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-06-14
 *
 * RecyclerView拖拽Item ViewModel
 */
class RecyclerViewDragViewModel: BaseViewModel() {

    private val _items = MutableStateFlow<List<String>>(listOf())
    val items: StateFlow<List<String>> get() = _items

    init {
        initData()
    }

    override fun refresh() {
        viewModelScope.launch {
            val generateItems = mutableListOf<String>(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12",
                "13",
                "14",
                "15",
            )
            _items.value = generateItems
        }
    }
}