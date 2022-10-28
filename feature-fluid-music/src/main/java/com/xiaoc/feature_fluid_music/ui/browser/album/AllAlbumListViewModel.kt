package com.xiaoc.feature_fluid_music.ui.browser.album

import android.content.ComponentName
import android.content.Context
import androidx.concurrent.futures.await
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.xiaoc.feature_fluid_music.connection.MusicServiceConnection
import com.xiaoc.feature_fluid_music.service.FluidMusicService
import com.zjl.base.globalContext
import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 所有专辑 展示列表 ViewModel
 **/
class AllAlbumListViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private val parentId = savedStateHandle["parentId"] ?: ""

    private val _localAllAlbumList = MutableStateFlow<List<MediaItem>>(emptyList())
    val localAllAlbumList: StateFlow<List<MediaItem>> = _localAllAlbumList

    private val musicServiceConnection = MusicServiceConnection.getInstance(globalContext)

    init {
        initData()
    }

    override fun initData() {
        viewModelScope.launch {
            val children = musicServiceConnection.getChildren(parentId)

            _localAllAlbumList.value = children
        }
    }
}