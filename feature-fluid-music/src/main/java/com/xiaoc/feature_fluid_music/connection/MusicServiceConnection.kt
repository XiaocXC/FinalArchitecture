package com.xiaoc.feature_fluid_music.connection

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.concurrent.futures.await
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.*
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import com.xiaoc.feature_fluid_music.service.FluidMusicService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author Xiaoc
 */
class MusicServiceConnection private constructor(
    private val context: Context
) {

    /**
     * 根内容的媒体信息
     * 当根内容媒体信息发生变化后，可以重新开始从根目录进行订阅
     */
    private val _rootMediaItem = MutableStateFlow(MediaItem.EMPTY)
    val rootMediaItem: StateFlow<MediaItem> = _rootMediaItem

    /**
     * 当前播放状态
     */
    private val _playbackState = MutableStateFlow(EMPTY_PLAYBACK_STATE)
    val playbackState: StateFlow<PlaybackState> = _playbackState

    /**
     * 当前播放媒体
     */
    private val _nowPlaying = MutableStateFlow(MediaItem.EMPTY)
    val nowPlaying: StateFlow<MediaItem> = _nowPlaying

    /**
     * 播放控制实例
     */
    val player: Player? get() = browser

    private var browser: MediaBrowser? = null
    private val playerListener: PlayerListener = PlayerListener()

    /**
     * 启动一个协程
     */
    private val coroutineContext: CoroutineContext = Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext + SupervisorJob())

    init {
        scope.launch {
            val newBrowser =
                MediaBrowser.Builder(context, SessionToken(context, ComponentName(context, FluidMusicService::class.java)))
                    .setListener(BrowserListener())
                    .buildAsync()
                    .await()
            newBrowser.addListener(playerListener)
            browser = newBrowser
            _rootMediaItem.value = newBrowser.getLibraryRoot(/* params= */ null).await().value ?: MediaItem.EMPTY
            newBrowser.currentMediaItem?.let {
                _nowPlaying.value = it
            }
        }
    }

    suspend fun getChildren(parentId: String, page: Int = 0, pageSize: Int = Int.MAX_VALUE): List<MediaItem> {
        return this.browser?.getChildren(parentId, page, pageSize, null)?.await()?.value
            ?: emptyList()
    }

    suspend fun sendCommand(command: String, parameters: Bundle?):Boolean =
        sendCommand(command, parameters) { _, _ -> }

    suspend fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ((Int, Bundle?) -> Unit)
    ):Boolean = if (browser?.isConnected == true) {
        val args = parameters ?: Bundle()
        browser?.sendCustomCommand(SessionCommand(command, args), args)?.await()?.let {
            resultCallback(it.resultCode, it.extras)
        }
        true
    } else {
        false
    }

    fun release() {
        _rootMediaItem.value = MediaItem.EMPTY
        _nowPlaying.value = MediaItem.EMPTY
        browser?.let {
            it.removeListener(playerListener)
            it.release()
        }
        instance = null
    }

    private fun updatePlaybackState(player: Player) {
        _playbackState.value = PlaybackState(
            player.playbackState,
            player.playWhenReady,
            player.duration
        )
    }

    private fun updateNowPlaying(player: Player) {
        val mediaItem = player.currentMediaItem ?: MediaItem.EMPTY
        if (mediaItem == MediaItem.EMPTY) {
            return
        }
        // 我们可以从这里获取到更详细的信息
        scope.launch {
            val mediaItemResult = browser!!.getItem(mediaItem.mediaId).await()
            val fullMediaItem = mediaItemResult.value ?: mediaItem
            _nowPlaying.value =
                mediaItem.buildUpon().setMediaMetadata(fullMediaItem.mediaMetadata).build()

        }
    }

    companion object {

        @Volatile
        private var instance: MusicServiceConnection? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: MusicServiceConnection(context)
                    .also { instance = it }
            }
    }

    private inner class BrowserListener : MediaBrowser.Listener {
        override fun onDisconnected(controller: MediaController) {
            release()
        }
    }

    private inner class PlayerListener : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (events.contains(EVENT_PLAY_WHEN_READY_CHANGED)
                || events.contains(EVENT_PLAYBACK_STATE_CHANGED)
                || events.contains(EVENT_MEDIA_ITEM_TRANSITION)) {
                updatePlaybackState(player)
            }
            if (events.contains(EVENT_MEDIA_METADATA_CHANGED)
                || events.contains(EVENT_MEDIA_ITEM_TRANSITION)
                || events.contains(EVENT_PLAY_WHEN_READY_CHANGED)) {
                updateNowPlaying(player)
            }
        }

        override fun onPlayerErrorChanged(error: PlaybackException?) {
        }
    }
}

class PlaybackState(
    private val playbackState: Int = Player.STATE_IDLE,
    private val playWhenReady: Boolean = false,
    val duration: Long = C.TIME_UNSET
) {
    val isPlaying: Boolean
        get() {
            return (playbackState == Player.STATE_BUFFERING
                    || playbackState == Player.STATE_READY)
                    && playWhenReady
        }
}

@Suppress("PropertyName")
val EMPTY_PLAYBACK_STATE: PlaybackState = PlaybackState()