package com.xiaoc.feature_fluid_music.ui.browser

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.xiaoc.feature_fluid_music.connection.MusicServiceConnection
import com.zjl.base.globalContext
import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 音乐浏览列表
 **/
class MusicBrowserViewModel: BaseViewModel() {

    private val musicServiceConnection = MusicServiceConnection.getInstance(globalContext)

    private val _mediaTypes = musicServiceConnection.rootMediaItem.map {
        val rootMediaItem = it

        val rootChildrenResult = musicServiceConnection.getChildren(rootMediaItem.mediaId, 0, Int.MAX_VALUE)
        rootChildrenResult
    }
    val mediaTypes: StateFlow<List<MediaItem>> = _mediaTypes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    override fun refresh() {

    }
}