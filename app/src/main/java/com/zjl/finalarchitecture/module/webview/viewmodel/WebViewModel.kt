package com.zjl.finalarchitecture.module.webview.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.model.ArticleListVO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Xiaoc
 * @since  2022-04-30
 *
 * WebView相关ViewModel
 **/
class WebViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private val _webUrlInner = savedStateHandle.get<ArticleListVO>("data")?.link

    private val _webUrl = MutableStateFlow(_webUrlInner ?: "")
    val webUrl: StateFlow<String> get() = _webUrl

    fun initData() {

    }
}