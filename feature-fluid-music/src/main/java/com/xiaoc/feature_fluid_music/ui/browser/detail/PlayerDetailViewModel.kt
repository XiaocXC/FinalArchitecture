package com.xiaoc.feature_fluid_music.ui.browser.detail

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.xiaoc.feature_fluid_music.connection.MusicServiceConnection
import com.zjl.base.globalContext
import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-09-01
 *
 * 播放详情ViewModel
 **/
class PlayerDetailViewModel: BaseViewModel() {

    private val _currentPlayInfo = MutableStateFlow<MediaItem?>(null)
    val currentPlayInfo: StateFlow<MediaItem?> = _currentPlayInfo

    private val musicServiceConnection = MusicServiceConnection.getInstance(globalContext)

    val currentPlaybackState = musicServiceConnection.playbackState

    init {
        initData()
    }

    fun getCurrentPosition(): Long{
        return musicServiceConnection.player?.currentPosition ?: 0L
    }

    /**
     * 播放暂停
     */
    fun pauseOrResume(){
        musicServiceConnection.player?.let {
            it.playWhenReady = !it.playWhenReady
        }
    }

    /**
     * 跳转到对应位置播放
     * @param position 时间轴
     */
    fun seekTo(position: Long){
        musicServiceConnection.player?.seekTo(position)
    }

    /**
     * 下一首
     */
    fun next(){
        musicServiceConnection.player?.seekToNext()
    }

    /**
     * 上一首
     */
    fun previous(){
        musicServiceConnection.player?.seekToPrevious()
    }

    override fun initData() {
        viewModelScope.launch {
            musicServiceConnection.nowPlaying.collectLatest {
                _currentPlayInfo.value = it
            }
        }
    }
}