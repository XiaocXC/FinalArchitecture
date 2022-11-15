package com.xiaoc.feature_fluid_music.ui.browser.music

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.xiaoc.feature_fluid_music.connection.MusicServiceConnection
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
 * 所有音乐 展示列表 ViewModel
 **/
class AllMusicListViewModel(
    savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private val parentId = savedStateHandle["parentId"] ?: ""

    private val musicServiceConnection = MusicServiceConnection.getInstance(globalContext)

    private val _localAllMusicList = MutableStateFlow<List<UIMediaData>>(emptyList())
    val localAllMusicList: StateFlow<List<UIMediaData>> = _localAllMusicList

    init {
        initData()
    }

    private fun updateCurrentMediaItem(mediaItem: MediaItem?){
        _localAllMusicList.value = _localAllMusicList.value.map {
            // 如果mediaItem的Id与列表中的其中一个相同，说明该项正在播放，isPlaying置为true
            it.copy(isPlaying = mediaItem?.mediaId == it.mediaItem.mediaId)
        }
    }

    /**
     * 播放播放列表中的某一项
     * @param index 播放列表中要播放项的index
     */
    fun playByList(index: Int){
        viewModelScope.launch {
            val playMediaItems = _localAllMusicList.value.map {
                it.mediaItem
            }
            musicServiceConnection.player?.let {
                it.setMediaItems(playMediaItems)
                it.shuffleModeEnabled = false
                it.prepare()
                it.seekToDefaultPosition(index)
                it.play()
            }
        }
    }

    fun initData() {
        viewModelScope.launch {
            val children = musicServiceConnection.getChildren(parentId)
            val playMediaItems = children.map {
                UIMediaData(it, false)
            }
            _localAllMusicList.value = playMediaItems
        }

        viewModelScope.launch(Dispatchers.Default) {
            musicServiceConnection.nowPlaying.collectLatest {
                updateCurrentMediaItem(it)
            }
        }
    }
}