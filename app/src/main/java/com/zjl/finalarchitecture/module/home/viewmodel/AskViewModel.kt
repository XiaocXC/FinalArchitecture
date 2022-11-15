package com.zjl.finalarchitecture.module.home.viewmodel

import com.zjl.base.ui.PagingUiModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.utils.ext.paging.requestPagingApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @description:
 * @author: zhou
 * @date : 2022/4/25 19:46
 */
class AskViewModel : PagingBaseViewModel() {

    private val _askList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val askList: StateFlow<PagingUiModel<ArticleListVO>> = _askList

    private var mCurrentIndex = initPageIndex()

    init {
        // 初始化时请求刷新数据
        onRefreshData()
    }

    override fun onRefreshData(tag: Any?) {
        mCurrentIndex = initPageIndex()
        requestAskArticle(mCurrentIndex)
    }

    override fun onLoadMoreData(tag: Any?) {
        ++ mCurrentIndex
        requestAskArticle(mCurrentIndex)
    }

    fun requestAskArticle(currentIndex: Int) {
        requestScope {
            requestPagingApiResult(isRefresh = currentIndex == initPageIndex(), pagingUiModel = _askList){
                ApiRepository.requestAskArticleListDataByPage(currentIndex)
            }.await()
        }
    }
}