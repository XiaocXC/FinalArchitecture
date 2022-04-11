package com.zjl.finalarchitecture.module.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlazaViewModel: BaseViewModel()  {

    val coldFlow = flow {
        emit(1)
        emit(2)
        emit(2)
        emit(2)
        emit(2)
        emit(2)
        emit(2)
    }.flowOn(Dispatchers.IO) // 以上内容在IO线程执行
        .map {

        }.map {

        }.flowOn(Dispatchers.Default)  // 以上Map内容在Default线程执行

    val hotFlow = MutableStateFlow(2)

    fun test(){
        viewModelScope.launch {
            coldFlow.collect {
                // 打印
                // 1  2
            }

            coldFlow.collect {
                // 打印
                // 1  2
            }

            hotFlow.collect {
                // 打印
                // 2

                // 执行下面的内容后
                // 5
            }

            hotFlow.value = 5
        }
    }

    // BannerUI
    private val _plazaListFlow = MutableStateFlow<MutableList<ArticleListVO>>(mutableListOf())
    val mPlazaListFlow: StateFlow<MutableList<ArticleListVO>> = _plazaListFlow

    // BannerUI
    private val _addPlazaListFlow = MutableStateFlow<List<ArticleListVO>>(emptyList())
    val mAddPlazaListFlow: StateFlow<List<ArticleListVO>> = _addPlazaListFlow

    private var currentPage = 0

    init {
        toRefresh()
    }

    /**
     * 获取广场文章
     */
    fun requestPlazaData(){
        viewModelScope.launch {
            launchRequestByNormal({
                ApiRepository.requestPlazaArticleDataByPage(currentPage)
            }, successBlock = {
//            _bannerListUiModel.value = it
                _addPlazaListFlow.value = it.dataList
                _plazaListFlow.value.addAll(it.dataList)
                _addPlazaListFlow.value = mutableListOf()

                currentPage = it.currentPage
            })
        }
    }


    override fun refresh() {
        currentPage = 0
        requestPlazaData()
    }
}