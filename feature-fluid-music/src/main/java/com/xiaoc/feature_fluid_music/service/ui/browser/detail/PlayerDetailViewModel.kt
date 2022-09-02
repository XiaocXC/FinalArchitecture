package com.xiaoc.feature_fluid_music.service.ui.browser.detail

import android.content.ComponentName
import android.content.Context
import androidx.concurrent.futures.await
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
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
 * @since  2022-09-01
 *
 * 播放详情ViewModel
 **/
class PlayerDetailViewModel: BaseViewModel() {

    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    val browser: MediaBrowser?
        get() = if (browserFuture.isDone) browserFuture.get() else null

    private val _currentPlayInfo = MutableStateFlow<MediaItem?>(null)
    val currentPlayInfo: StateFlow<MediaItem?> = _currentPlayInfo

    private val _currentIsPlaying = MutableStateFlow(false)
    val currentIsPlaying: StateFlow<Boolean> = _currentIsPlaying

    private val playerListener = object: Player.Listener{
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateCurrentPlayerInfo(mediaItem)
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            updateCurrentPlayerState(playWhenReady)
        }
    }

    fun pauseOrResume(){
        val browser = browser ?: return
        browser.playWhenReady = !browser.isPlaying
    }

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

            setControllerListener(browser)
        }
    }

    private fun setControllerListener(browser: MediaBrowser){
        // 立即更新一下当前数据
        updateCurrentPlayerInfo(browser.currentMediaItem)
        updateCurrentPlayerState(browser.playWhenReady)

        // 监听数据变化
        browser.addListener(playerListener)
    }

    private fun updateCurrentPlayerInfo(mediaItem: MediaItem?){
        _currentPlayInfo.value = mediaItem
    }

    private fun updateCurrentPlayerState(isPlaying: Boolean){
        _currentIsPlaying.value = isPlaying
    }

    fun releaseBrowser(){
        MediaBrowser.releaseFuture(browserFuture)
    }

    override fun refresh() {

    }
}