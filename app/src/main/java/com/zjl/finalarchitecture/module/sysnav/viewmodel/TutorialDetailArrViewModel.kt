package com.zjl.finalarchitecture.module.sysnav.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.SystemVO
import com.zjl.finalarchitecture.data.model.TutorialVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.utils.ext.paging.requestPagingApiResult
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @description:
 * @author: zhou
 * @date : 2022/11/17 20:23
 */
class TutorialDetailArrViewModel(
    savedStateHandle: SavedStateHandle
) : PagingBaseViewModel() {


    /**
     * 子栏目的 id，通过[savedStateHandle]获取从上一个Fragment传来
     */
    private val tutorialIds: Int? = savedStateHandle["ids"]


    /**
     * 教程详细列表数据
     */
    private val _tutorialDetailList =
        MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val tutorialDetailList: StateFlow<PagingUiModel<ArticleListVO>> = _tutorialDetailList


    private var mCurrentIndex = initPageIndex()

    init {
        onRefreshData()
    }


    private fun loadTutorialDetailListData(currentIndex: Int) {
        requestScope {
            requestPagingApiResult(
                isRefresh = currentIndex == initPageIndex(),
                pagingUiModel = _tutorialDetailList
            ) {
                ApiRepository.requestTutorialDetailListData(page = currentIndex, cid = tutorialIds)
            }.await()
        }.cancel()
    }

    override fun onRefreshData(tag: Any?) {
        mCurrentIndex = initPageIndex()
        loadTutorialDetailListData(mCurrentIndex)
    }

    override fun onLoadMoreData(tag: Any?) {
        ++mCurrentIndex
        loadTutorialDetailListData(mCurrentIndex)
    }


}