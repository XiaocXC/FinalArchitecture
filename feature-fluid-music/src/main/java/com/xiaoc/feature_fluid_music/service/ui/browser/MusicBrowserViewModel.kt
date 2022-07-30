package com.xiaoc.feature_fluid_music.service.ui.browser

import android.content.ComponentName
import android.content.Context
import androidx.concurrent.futures.await
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.xiaoc.feature_fluid_music.service.FluidMusicService
import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 音乐浏览列表
 **/
class MusicBrowserViewModel: BaseViewModel() {

    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    val browser: MediaBrowser?
        get() = if (browserFuture.isDone) browserFuture.get() else null

    private val _mediaTypes = MutableStateFlow(emptyList<MediaItem>())
    val mediaTypes: StateFlow<List<MediaItem>> = _mediaTypes

    fun initializeBrowser(context: Context){
        viewModelScope.launch {
            browserFuture =
                MediaBrowser.Builder(
                    context,
                    SessionToken(context, ComponentName(context, FluidMusicService::class.java))
                ).buildAsync()

            browserFuture.await()

            // 获取根Root的浏览内容
            val browser = browser ?: return@launch
            val rootResult = browser.getLibraryRoot(null).await()
            val rootMediaItem = rootResult.value ?: return@launch

            val rootChildrenResult = browser.getChildren(rootMediaItem.mediaId, 0, Int.MAX_VALUE, null).await()
            val rootChildren = rootChildrenResult.value ?: return@launch

            _mediaTypes.value = rootChildren
        }
    }

    fun releaseBrowser(){
        MediaBrowser.releaseFuture(browserFuture)
    }

    override fun refresh() {

    }
}