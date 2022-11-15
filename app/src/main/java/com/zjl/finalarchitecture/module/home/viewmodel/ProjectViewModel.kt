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

/**
 * @description:
 * @author: zhou
 * @date : 2022/4/25 19:46
 */
class ProjectViewModel: PagingBaseViewModel() {

    /**
     * 项目分类数据
     */
    private val _categoryList = MutableStateFlow<UiModel<List<CategoryVO>>>(UiModel.Loading())
    val categoryList: StateFlow<UiModel<List<CategoryVO>>> = _categoryList

    /**
     * 项目分类列表数据
     */
    private val _articleList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
    val articleList: StateFlow<PagingUiModel<ArticleListVO>> = _articleList

    private var categoryId: Int? = null

    private var mCurrentIndex = initPageIndex()

    //项目分类选中下表
    var checkPosition = 0

    fun refreshCategoryList(cid: Int){
        categoryId = cid
        // 刷新数据
        onRefreshData()
    }

    init {
        requestCategory()
    }

    private fun loadMoreProjectList(currentIndex: Int) {
        if(categoryId == null){
            return
        }
        requestScope {
            requestPagingApiResult(isRefresh = currentIndex == initPageIndex(), pagingUiModel = _articleList) {
                ApiRepository.requestProjectDetailListDataByPage(currentIndex, categoryId!!)
            }.await()
        }
    }

    override fun initPageIndex(): Int {
        return 1
    }

    override fun onRefreshData(tag: Any?) {
        mCurrentIndex = initPageIndex()
        loadMoreProjectList(mCurrentIndex)
    }

    override fun onLoadMoreData(tag: Any?) {
        mCurrentIndex ++
        loadMoreProjectList(mCurrentIndex)
    }

    /**
     * 请求项目分类
     */
    private fun requestCategory() {
        requestScope {
            val data = requestApiResult(uiModel = _categoryList) {
                ApiRepository.requestProjectListData()
            }.await()

            if(data.isNotEmpty()){
                refreshCategoryList(data[0].id)
            }
        }
    }

}