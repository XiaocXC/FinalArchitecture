package com.xiaoc.feature_fluid_music.ui.browser.artist

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
import com.xiaoc.feature_fluid_music.ui.bean.UIMediaData
import com.zjl.base.globalContext
import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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

    private val musicServiceConnection = MusicServiceConnection.getInstance(globalContext)

    private val _localAllArtistList = MutableStateFlow<List<MediaItem>>(emptyList())
    val localAllArtistList: StateFlow<List<MediaItem>> = _localAllArtistList

    init {
        initData()
    }

    override fun initData() {

        viewModelScope.launch {
            val children = musicServiceConnection.getChildren(parentId)

            _localAllArtistList.value = children
        }
    }
}