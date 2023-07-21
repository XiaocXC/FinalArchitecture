package com.zjl.finalarchitecture.module.mine.viewmodel

import com.zjl.base.ui.PagingUiModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.CoinRecordVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.utils.ext.paging.requestPagingApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author: 周健力
 * @e-mail: zhoujl@eetrust.com
 * @time: 2023/7/21
 * @version: 1.0
 * @description: 积分排行ViewModel
 */
class RankViewModel : PagingBaseViewModel() {

    private val _rankList =
        MutableStateFlow<PagingUiModel<CoinRecordVO>>(PagingUiModel.Loading(true))
    val rankListStateFlow: StateFlow<PagingUiModel<CoinRecordVO>> = _rankList

    fun initData() {
        onRefreshData()
    }

    override fun initPageIndex(): Int {
        return 1
    }

    private var mCurrentIndex = initPageIndex()

    override fun onRefreshData(tag: Any?) {
        mCurrentIndex = initPageIndex()
        loadRankList(mCurrentIndex)
    }

    override fun onLoadMoreData(tag: Any?) {
        ++mCurrentIndex
        loadRankList(mCurrentIndex)
    }

    private fun loadRankList(currentIndex: Int) {
        requestScope {
            requestPagingApiResult(
                isRefresh = currentIndex == initPageIndex(), pagingUiModel = _rankList
            ) {
                ApiRepository.requestCoinRecordList(page = currentIndex)
            }.await()
        }
    }
}