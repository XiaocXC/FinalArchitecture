package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.CategoryVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class WechatViewModel : PagingBaseViewModel() {

    /**
     * 微信公众号分类列表数据
     */
    private val _categoryList = MutableStateFlow<List<CategoryVO>>(emptyList())
    val categoryList: StateFlow<List<CategoryVO>> = _categoryList

    /**
     * 微信分类列表数据
     */
    private val _articleList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val articleList: StateFlow<PagingUiModel<ArticleListVO>> = _articleList

    private val _cid = MutableStateFlow(0)
    val cid: StateFlow<Int> = _cid

    // 微信公众号分类选中下表
    var checkPosition = 0

    init {
        requestCategory()
    }

    override fun initPageIndex(): Int {
        return 1
    }

    override fun loadMoreInner(currentIndex: Int) {
        viewModelScope.launch {
            launchRequestByPaging({
                ApiRepository.requestWechatDetailListDataByPage(currentIndex, _cid.value)
            }, successBlock = {
                _articleList.value = PagingUiModel.Success(it.dataList, currentIndex == initPageIndex(), !it.over)
            }, failureBlock = {
                _articleList.value = PagingUiModel.Error(ApiException(it), currentIndex == initPageIndex())
            })
        }
    }

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

    /**
     * 请求微信公众号分类
     */
    private fun requestCategory() {
        viewModelScope.launch {
            launchRequestByNormal({
                ApiRepository.requestWechatListData()
            }, successBlock = { data ->
                // 状态更改为成功
                _rootViewState.emit(UiModel.Success(data))
                _categoryList.value = data
                // 默认选中一个
                if(data.isNotEmpty()){
                    onCidChanged(data[0].id)
                }
            },failureBlock = { error ->
                // 状态更改为错误
                _rootViewState.emit(UiModel.Error(ApiException(error)))
            })
        }
    }

}