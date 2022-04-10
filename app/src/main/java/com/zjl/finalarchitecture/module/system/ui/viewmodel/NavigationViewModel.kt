package com.zjl.finalarchitecture.module.system.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.model.navigation.NavigationVO
import com.zjl.finalarchitecture.module.home.repository.resp.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-04-07
 *
 * 导航界面 ViewModel
 */
class NavigationViewModel: BaseViewModel() {

    /**
     * 体系列表数据
     */
    private val _navigationList = MutableStateFlow<List<NavigationVO>>(emptyList())
    val navigationList: StateFlow<List<NavigationVO>> = _navigationList

    init {
        toRefresh()
    }

    override fun refresh() {

        viewModelScope.launch {
            launchRequestByNormal({
                HomeRepository.requestNavigationListData()
            },{ data ->
                // 状态更改为成功
                _rootViewState.emit(UiModel.Success(data))
                _navigationList.value = data
            },{ error ->
                // 状态更改为错误
                _rootViewState.emit(UiModel.Error(ApiException(error)))
            })
        }
    }
}