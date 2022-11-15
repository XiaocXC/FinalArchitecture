package com.zjl.finalarchitecture.module.sysnav.viewmodel

import androidx.lifecycle.viewModelScope
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.NavigationVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-04-07
 *
 * 导航界面 ViewModel
 */
class NavigationViewModel : BaseViewModel() {

    private val _navigationList = MutableStateFlow<List<NavigationVO>>(emptyList())
    val navigationList: StateFlow<List<NavigationVO>> = _navigationList

    private val _fuckNavigationList =
        MutableStateFlow<UiModel<List<NavigationVO>>>(UiModel.Loading())
    val fuckNavigationList: StateFlow<UiModel<List<NavigationVO>>> = _fuckNavigationList

    init {
        initData()
    }

    fun initData() {
        requestScope {
            requestApiResult(uiModel = _fuckNavigationList) {
                ApiRepository.requestNavigationListData()
            }.await()
        }
    }
}