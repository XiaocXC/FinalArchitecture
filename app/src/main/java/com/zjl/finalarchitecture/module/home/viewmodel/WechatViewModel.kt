package com.zjl.finalarchitecture.module.home.viewmodel

import com.zjl.base.ui.PagingUiModel
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.PagingBaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.CategoryVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.utils.ext.paging.requestPagingApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WechatViewModel : PagingBaseViewModel() {

    /**
     * 微信公众号分类列表数据
     */
    private val _categoryList = MutableStateFlow<UiModel<List<CategoryVO>>>(UiModel.Loading())
    val categoryList: StateFlow<UiModel<List<CategoryVO>>> = _categoryList

    /**
     * 微信分类列表数据
     */
    private val _articleList =
        MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val articleList: StateFlow<PagingUiModel<ArticleListVO>> = _articleList

    private var categoryId: Int? = null

    private var mCurrentIndex = initPageIndex()

    // 微信公众号分类选中下表
    var checkPosition = 0

    init {
        requestCategory()
    }

    override fun initPageIndex(): Int {
        return 1
    }

    override fun onRefreshData(tag: Any?) {
        mCurrentIndex = initPageIndex()
        loadMoreWechatList(mCurrentIndex)
    }

    override fun onLoadMoreData(tag: Any?) {
        mCurrentIndex ++
        loadMoreWechatList(mCurrentIndex)
    }

    private fun loadMoreWechatList(currentIndex: Int) {
        requestScope {
            requestPagingApiResult(isRefresh = currentIndex == initPageIndex(), pagingUiModel = _articleList){
                ApiRepository.requestWechatDetailListDataByPage(categoryId!!, currentIndex)
            }.await()
        }
    }

    /**
     * 点击后更改的CategoryId
     * 更改会会触发加载数据
     */
    fun onRefreshCategoryId(cid: Int) {
        categoryId = cid
        // 刷新数据
        onRefreshData()
    }

    /**
     * 请求微信公众号分类
     */
    private fun requestCategory() {
        requestScope {
            val data = requestApiResult(uiModel = _categoryList) {
                ApiRepository.requestWechatListData()
            }.await()

            // 默认选中一个
            if(data.isNotEmpty()){
                onRefreshCategoryId(data[0].id)
            }
        }
    }

}