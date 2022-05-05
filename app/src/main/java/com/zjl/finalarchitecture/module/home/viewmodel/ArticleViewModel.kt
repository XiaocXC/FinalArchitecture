package com.zjl.finalarchitecture.module.home.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.zjl.base.utils.WhileViewSubscribed
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.model.BannerVO
import com.zjl.finalarchitecture.data.model.event.ArticleListEvent
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.ArticlePagingSource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

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

    private val modificationEvents = MutableStateFlow<List<ArticleListEvent>>(mutableListOf())

    // 文章
    private val _articlePagingFlow = Pager(PagingConfig(pageSize = 20)) {
        ArticlePagingSource()
    }.flow.cachedIn(viewModelScope).combine(modificationEvents){ pagingData, modifications ->
            modifications.fold(pagingData){ acc, event ->
                handleArticleEvent(acc, event)
            }
    }

    val articlePagingFlow: LiveData<PagingData<ArticleListVO>> = _articlePagingFlow.asLiveData()

    init {
        initData()
    }

    override fun refresh() {
        // 请求中状态
        refreshBanner()
    }

    fun updateCollectState(id: Int, isCollect: Boolean){
        modificationEvents.value += ArticleListEvent.ArticleCollectEvent(id, isCollect)
    }

    /**
     * 刷新Banner数据
     */
    private fun refreshBanner(){
        viewModelScope.launch {
            launchRequestByNormal({
                ApiRepository.requestBanner()
            }, successBlock = {
                _bannerList.value = it
            })
        }
    }

    private fun handleArticleEvent(
        pagingData: PagingData<ArticleListVO>,
        event: ArticleListEvent
    ): PagingData<ArticleListVO> {
        return when (event) {
            is ArticleListEvent.ArticleCollectEvent -> {
                pagingData.map {
                    if (it.id == event.id) {
                        return@map it.copy(collect = event.isCollect)
                    } else {
                        return@map it
                    }
                }
            }
        }

    }
}