package com.zjl.finalarchitecture.module.home.viewmodel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.home.model.BannerVO
import com.zjl.finalarchitecture.module.home.repository.resp.HomeRepository
import com.zjl.finalarchitecture.module.home.repository.datasouce.ArticlePagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleViewModel : BaseViewModel() {

    // BannerUI
//    private val _bannerListUiModel = MutableLiveData<List<BannerVO>>()
//    val bannerListUiModel: LiveData<List<BannerVO>> get() = _bannerListUiModel

    // BannerUI
    private val _bannerList = MutableStateFlow<List<BannerVO>>(emptyList())
    val bannerList: StateFlow<List<BannerVO>> = _bannerList

    // 文章
    val articlePagingFlow = Pager(PagingConfig(pageSize = 20)) {
        ArticlePagingSource()
    }.flow.cachedIn(viewModelScope)

    init {
        toRefresh()
    }

    override fun refresh() {
        // 请求中状态
        refreshBanner()
    }

    /**
     * 刷新Banner数据
     */
    private fun refreshBanner(){
        viewModelScope.launch {
            launchRequestByNormal({
                HomeRepository.requestBanner()
            }, successBlock = {
//            _bannerListUiModel.value = it
                _bannerList.value = it
            })
        }

    }
}