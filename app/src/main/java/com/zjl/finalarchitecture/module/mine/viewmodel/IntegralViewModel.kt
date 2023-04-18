package com.zjl.finalarchitecture.module.mine.viewmodel

import com.zjl.base.ui.PagingUiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.CoinRecordVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import com.zjl.finalarchitecture.utils.ext.paging.requestPagingApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @description:
 * @author: zhou
 * @date : 2022/11/30 20:35
 */
class IntegralViewModel : PagingBaseViewModel() {

    val userInfo = UserAuthDataSource.basicUserInfoVO

    /**
     * 教程详细列表数据
     */
    private val _coinRecordList =
        MutableStateFlow<PagingUiModel<CoinRecordVO>>(PagingUiModel.Loading(true))
    val coinRecordList: StateFlow<PagingUiModel<CoinRecordVO>> = _coinRecordList

    fun initData() {
        onRefreshData()
    }

    override fun initPageIndex(): Int {
        return 1
    }

    private var mCurrentIndex = initPageIndex()

    override fun onRefreshData(tag: Any?) {
        mCurrentIndex = initPageIndex()
        loadCoinRecordListData(mCurrentIndex)
    }

    override fun onLoadMoreData(tag: Any?) {
        ++mCurrentIndex
        loadCoinRecordListData(mCurrentIndex)
    }

    private fun loadCoinRecordListData(currentIndex: Int) {
        requestScope {
            requestPagingApiResult(
                isRefresh = currentIndex == initPageIndex(),
                pagingUiModel = _coinRecordList
            ) {
                ApiRepository.requestCoinRecordList(page = currentIndex)
            }.await()
        }
    }

}