package com.zjl.finalarchitecture.module.webview.viewmodel

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.model.ArticleListVO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize

/**
 * @author Xiaoc
 * @since  2022-04-30
 *
 * WebView相关ViewModel
 **/
class WebViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private val _webUrlInner: String? = savedStateHandle.get<Parcelable>("data")?.let {
        when (it) {
            is ArticleListVO -> {
                it.link
            }
            is WebDataUrl -> {
                it.url
            }
            else -> {
                ""
            }
        }
    }

    private val _webUrl = MutableStateFlow(_webUrlInner)
    val webUrl: StateFlow<String?> get() = _webUrl

    fun initData() {

    }
}

@Parcelize
data class WebDataUrl(
    val url: String,
    val title: String?
): Parcelable