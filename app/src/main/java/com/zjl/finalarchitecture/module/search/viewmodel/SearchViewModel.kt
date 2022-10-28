package com.zjl.finalarchitecture.module.search.viewmodel

import androidx.lifecycle.viewModelScope
import com.zjl.base.utils.globalJson
import com.zjl.finalarchitecture.utils.CacheUtil
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.model.SearchHotVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString

/**
 * @author Xiaoc
 * @since  2022-05-03
 **/
class SearchViewModel: BaseViewModel() {

    private val _searchHotKeys = MutableStateFlow<List<SearchHotVO>>(emptyList())
    val searchHotKeys: StateFlow<List<SearchHotVO>> get() = _searchHotKeys

    private val _searchHistoryKeys = MutableStateFlow<MutableList<String>>(mutableListOf())
    val searchHistoryKeys: StateFlow<List<String>> get() = _searchHistoryKeys

    init {
        initData()
    }

    fun saveSearchKey(key: String){
        viewModelScope.launch {
            // 查找搜索历史中是否已存在该搜索此
            val existKey = _searchHistoryKeys.value.find {
                it == key
            }
            val cacheKeys = _searchHistoryKeys.value.toMutableList()
            // 如果搜索的历史记录超过15条，我们移除最后一条
            if(cacheKeys.size > 15){
                cacheKeys.removeLast()
            }
            if(existKey == null){
                // 不存在我们直接插入到最前面
                cacheKeys.add(0, key)
            } else {
                // 如果存在我们将其删除，然后插入到最前面
                cacheKeys.remove(existKey)
                cacheKeys.add(0, key)
            }
            // 存储
            CacheUtil.setSearchHistoryData(
                globalJson.encodeToString(cacheKeys)
            )

            _searchHistoryKeys.value = cacheKeys
        }
    }

    fun removeSearchKey(key: String){
        viewModelScope.launch {
            // 查找搜索历史中是否已存在该搜索此
            val cacheKeys = _searchHistoryKeys.value.toMutableList()
            if(cacheKeys.remove(key)){
                // 存储
                CacheUtil.setSearchHistoryData(
                    globalJson.encodeToString(cacheKeys)
                )
                _searchHistoryKeys.value = cacheKeys
            }
        }
    }

    override fun initData() {
        viewModelScope.launch {
            launchRequestByNormal({
                ApiRepository.getSearchHot()
            }, successBlock = {
                _searchHotKeys.emit(it)
            })
        }
        viewModelScope.launch {
            _searchHistoryKeys.value = CacheUtil.getSearchHistoryData().toMutableList()
        }
    }
}