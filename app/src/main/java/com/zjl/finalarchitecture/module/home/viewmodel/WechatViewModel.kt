package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.model.CategoryVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.ProjectArticlePagingSource
import com.zjl.finalarchitecture.data.respository.datasouce.WeChatArticlePagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class WechatViewModel : BaseViewModel() {

    /**
     * 微信公众号分类列表数据
     */
    private val _categoryList = MutableStateFlow<List<CategoryVO>>(emptyList())
    val categoryList: StateFlow<List<CategoryVO>> = _categoryList

    private val _cid = MutableStateFlow(2)

    //微信公众号分类选中下表
    var checkPosition = 0

    /**
     * 微信公众号列表数据
     * 当Cid发生变化时，会自动重新请求Paging数据
     */
    val wechatArticlePagingFlow = _cid.flatMapLatest {
        Pager(PagingConfig(pageSize = 20)) {
            WeChatArticlePagingSource(it)
        }.flow
    }.cachedIn(viewModelScope)

    init {
        requestCategory()
    }

    override fun refresh() {

    }

    /**
     * 点击后更改的Cid
     * Cid更改会触发 [flatMapLatest] 请求分页
     */
    fun onCidChanged(cid: Int){
        _cid.value = cid
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
            },failureBlock = { error ->
                // 状态更改为错误
                _rootViewState.emit(UiModel.Error(ApiException(error)))
            })
        }
    }

}