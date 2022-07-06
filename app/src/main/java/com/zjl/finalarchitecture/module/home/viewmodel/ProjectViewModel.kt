package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.finalarchitecture.api.ApiService
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.CategoryVO
import com.zjl.finalarchitecture.data.model.SystemVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.AskPagingSource
import com.zjl.finalarchitecture.data.respository.datasouce.PlazaPagingSource
import com.zjl.finalarchitecture.data.respository.datasouce.ProjectArticlePagingSource
import com.zjl.finalarchitecture.data.respository.datasouce.SystemArticlePagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * @description:
 * @author: zhou
 * @date : 2022/4/25 19:46
 */
class ProjectViewModel: PagingBaseViewModel() {

    /**
     * 项目分类列表数据
     */
    private val _categoryList = MutableStateFlow<UiModel<List<CategoryVO>>>(UiModel.Loading())
    val categoryList: StateFlow<UiModel<List<CategoryVO>>> = _categoryList

    /**
     * 项目分类列表数据
     */
    private val _articleList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val articleList: StateFlow<PagingUiModel<ArticleListVO>> = _articleList

    private val _cid = MutableStateFlow(0)
    val cid: StateFlow<Int> = _cid

    //项目分类选中下表
    var checkPosition = 0

    /**
     * 点击后更改的Cid
     * Cid更改会触发 [flatMapLatest] 请求分页
     */
    fun onCidChanged(cid: Int){
        _cid.value = cid

        // 重置分页Index
        currentPageIndex = initPageIndex()
        loadMore()
    }

    init {
        requestCategory()
    }

    override fun loadMoreInner(currentIndex: Int) {
        viewModelScope.launch {
            // 如果是重新刷新，则显示加载中
            if(currentIndex == initPageIndex()){
                _articleList.value = PagingUiModel.Loading(true)
            }
            launchRequestByPaging({
                ApiRepository.requestProjectDetailListDataByPage(currentIndex, _cid.value)
            }, successBlock = {
                _articleList.value = PagingUiModel.Success(it.dataList, currentIndex == initPageIndex(), !it.over)
            }, failureBlock = {
                _articleList.value = PagingUiModel.Error(ApiException(it), currentIndex == initPageIndex())
            })
        }
    }

    override fun initPageIndex(): Int {
        return 1
    }

    /**
     * 请求项目分类
     */
    private fun requestCategory() {
        viewModelScope.launch {
            launchRequestByNormalWithUiState({
                ApiRepository.requestProjectListData()
            }, _categoryList, true, true, successBlock = { data ->

                // 默认选中一个
                if(data.isNotEmpty()){
                    onCidChanged(data[0].id)
                }
            })

        }
    }

}