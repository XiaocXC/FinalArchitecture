package com.xiaoc.feature_fluid_music.service.ui.browser.artist

import android.content.ComponentName
import android.content.Context
import androidx.concurrent.futures.await
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.xiaoc.feature_fluid_music.service.FluidMusicService
import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 所有歌手 展示列表 ViewModel
 **/
class AllArtistListViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private val parentId = savedStateHandle["parentId"] ?: ""

    private val _localAllArtistList = MutableStateFlow<List<MediaItem>>(emptyList())
    val localAllArtistList: StateFlow<List<MediaItem>> = _localAllArtistList

    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    val browser: MediaBrowser?
        get() = if (browserFuture.isDone) browserFuture.get() else null

    fun initializeBrowser(context: Context){
        viewModelScope.launch {
            browserFuture =
                MediaBrowser.Builder(
                    context,
                    SessionToken(context, ComponentName(context, FluidMusicService::class.java))
                ).buildAsync()

            browserFuture.await()

            // 获取对应parentId下的内容
            val browser = browser ?: return@launch

            val childrenResult = browser.getChildren(parentId, 0, Int.MAX_VALUE, null).await()
            val children = childrenResult.value ?: return@launch

            _localAllArtistList.value = children
        }
    }

    fun releaseBrowser(){
        MediaBrowser.releaseFuture(browserFuture)
    }

    override fun refresh() {

    }
}