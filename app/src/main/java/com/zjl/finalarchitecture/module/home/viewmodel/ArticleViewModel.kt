package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.repository.HomeRepository
import com.zjl.finalarchitecture.utils.requestByNormal
import com.zjl.library_network.exception.ApiException
import com.zjl.library_network.map
import com.zjl.library_network.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleViewModel : BaseViewModel() {

    //页码 首页数据页码从0开始
    var pageNo = 0

    // BannerUI
    private val _bannerListUiModel = MutableLiveData<List<BannerVO>>()
    val bannerListUiModel: LiveData<List<BannerVO>> get() = _bannerListUiModel

    // ArticleDataUI
    private val _articleListUiModel = MutableLiveData<List<ArticleListVO>>()
    val articleListUiModel: LiveData<List<ArticleListVO>> get() = _articleListUiModel

    init {
        pageNo = 0
        toReFresh()
    }

    /**
     * 刷新
     */
    fun toReFresh() {
        viewModelScope.launch {
            requestByNormal({
                // 请求中状态
                _rootViewState.value = UiModel.Loading()

                // 请求banner数据
                val bannerDeferred = async {
                    HomeRepository.requestBanner()
                }
                // 请求article文章数据
                val articleListDeferred = async {
                    HomeRepository.requestArticleByPageData(pageNo)
                }
                // banner数据结果
                val bannerList = mutableListOf<BannerVO>()
                bannerDeferred.await().onSuccess {
                    bannerList.addAll(it)
                }

                // 文章数据结果
                val articleDataResult = articleListDeferred.await()
                // 返回ArticleHomeData包裹类，内含banner数据集和article数据集，方便使用
                return@requestByNormal articleDataResult.map {
                    ArticleHomeData(bannerList, it.dataList)
                }

            }, successBlock = {
                // 成功状态
                _rootViewState.value = UiModel.Success(Unit)

                _bannerListUiModel.value = it.bannerList ?: emptyList()
                _articleListUiModel.value = it.articleList
            }, failureBlock = {
                // 失败状态
                _rootViewState.value = UiModel.Error(ApiException(it))
            })
        }
    }
}

data class ArticleHomeData(
    val bannerList: List<BannerVO>?,
    val articleList: List<ArticleListVO>
)