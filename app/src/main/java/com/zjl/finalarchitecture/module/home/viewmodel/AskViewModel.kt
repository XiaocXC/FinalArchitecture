package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.AskPagingSource
import com.zjl.finalarchitecture.data.respository.datasouce.PlazaPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @description:
 * @author: zhou
 * @date : 2022/4/25 19:46
 */
class AskViewModel : PagingBaseViewModel() {

    private val _askList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val askList: StateFlow<PagingUiModel<ArticleListVO>> = _askList

    init {
        initData()
    }

    override fun loadMoreInner(currentIndex: Int) {
        viewModelScope.launch {
            // 先置为加载中
            if(currentIndex == initPageIndex()){
                _askList.value = PagingUiModel.Loading(true)
            }

            launchRequestByPaging({
                ApiRepository.requestAskArticleListDataByPage(currentIndex)
            }, successBlock = {
                _askList.value = PagingUiModel.Success(it.dataList, currentIndex == initPageIndex(), !it.over)
            }, failureBlock = {
                _askList.value = PagingUiModel.Error(ApiException(it), currentIndex == initPageIndex())
            })
        }
    }
}