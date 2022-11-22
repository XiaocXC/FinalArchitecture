package com.zjl.finalarchitecture.module.sysnav.viewmodel

import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.SystemVO
import com.zjl.finalarchitecture.data.model.TutorialVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @description:
 * @author: zhou
 * @date : 2022/11/16 17:18
 */
class TutorialViewModel : BaseViewModel() {

    /**
     * 教程列表数据
     */
    private val _tutorialList = MutableStateFlow<UiModel<List<TutorialVO>>>(UiModel.Loading())
    val tutorialList: StateFlow<UiModel<List<TutorialVO>>> = _tutorialList

    init {
        loadTutorialListData()
    }

    /**
     * 加载教程数据
     */
    fun loadTutorialListData() {
        requestScope {
            requestApiResult(uiModel = _tutorialList) {
                ApiRepository.requestTutorialListData()
            }.await()
        }
    }



}